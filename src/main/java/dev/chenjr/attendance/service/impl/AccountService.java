package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.entity.Account;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.AccountMapper;
import dev.chenjr.attendance.dao.mapper.RoleMapper;
import dev.chenjr.attendance.dao.mapper.UserRoleMapper;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.exception.UserNotFoundException;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.BindThirdPartyDTO;
import dev.chenjr.attendance.service.dto.LoginDTO;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.TokenUidDTO;
import dev.chenjr.attendance.utils.JwtTokenUtil;
import dev.chenjr.attendance.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication Service 认证服务，处理身份认证(登入登出，用户密码校验，Token校验，权限分配等)
 */
@Service
@Slf4j
public class AccountService extends BaseService implements IAccountService {
    
    
    public static final String DEFAULT_PASSWORD = "123456";
    @Autowired
    IUserService userService;
    
    @Autowired
    ISmsService smsService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleService roleService;
    
    
    /**
     * 初始化用户，创建account
     */
    @Override
    public void initUser(long uid) {
        // 4. 创建Account
        this.setUserPassword(uid, DEFAULT_PASSWORD);
    }
    
    /**
     * 注册
     * 1. 检查验证码
     * 2. 密码设置
     * 3. 创建用户
     * 4. 创建Account
     * 5. 创建角色关系
     *
     * @param request 注册信息
     * @return token和uid
     */
    @Override
    public TokenUidDTO register(RegisterRequest request) {
        // 1. 检查验证码 前置到controller中
        
        // 2. 设置默认密码
        if (StringUtil.empty(request.getPassword())) {
            request.setPassword(DEFAULT_PASSWORD);
        }
        
        // 3. 创建用户
        User user = userService.createUser(request);
        
        // 4. 创建Account
        this.setUserPassword(user, request.getPassword());
        
        // 5. 创建角色关系
        List<String> roles = request.getRoles();
        for (String roleCode : roles) {
            roleService.addRoleByCodeToUser(user.getId(), roleCode);
        }
        
        String token = this.createToken(user);
        return new TokenUidDTO(token, user.getId());
        
    }
    
    /**
     * 检验账号和密码是否匹配
     *
     * @param account     邮箱或者手机号
     * @param rawPassword 未加密的密码
     * @return 是否匹配
     */
    @Override
    public boolean checkPasswordAndAccount(String account, String rawPassword) {
        log.info("Checking: " + account + " with " + rawPassword);
        User user = userService.getUserByAccount(account);
        if (user == null) {
            log.info("User not found!");
            return false;
        }
        Account accountInfo;
        accountInfo = accountMapper.getByUid(user.getId());
        if (accountInfo == null) {
            log.info("localAuth not found!");
            
            return false;
        }
        String encoded = accountInfo.getToken();
        return checkPasswordHash(encoded, rawPassword);
    }
    
    
    private String encodeHash(String password) {
        
        return passwordEncoder.encode(password);
    }
    
    @Override
    public Account getOneAccountInfo(long uid) {
        return this.accountMapper.getByUid(uid);
    }
    
    @Override
    public List<Account> getAllAccountInfo(long uid) {
        return accountMapper.getAllByUid(uid);
    }
    
    /**
     * 设置(修改)用户密码
     * 注册的时候用这个方法会新建所有的Account
     *
     * @param uid      user id
     * @param password 明文密码
     */
    @Override
    public void setUserPassword(long uid, String password) {
        
        boolean exists = userService.userExists(uid);
        if (!exists) {
            throw new UserNotFoundException();
        }
        
        dev.chenjr.attendance.dao.entity.User user = userService.getUserById(uid);
        this.setUserPassword(user, password);
    }
    
    @Override
    public void setUserPasswordWithSmsCode(@NotNull User user, String password, String code) {
        if (user == null) {
            throw new UserNotFoundException("got empty user!");
        }
        smsService.codeValidAndThrow(user.getPhone(), smsService.TYPE_RESET_PASSWORD, code);
        setUserPassword(user, password);
        
    }
    
    /**
     * 创建用户的Account
     * 注册的时候用这个方法会新建的Account
     *
     * @param user user 实体
     */
    @Override
    public void setUserPassword(User user) {
        this.setUserPassword(user, DEFAULT_PASSWORD);
    }
    
    /**
     * 设置(修改)用户密码
     * 注册的时候用这个方法会新建所有的Account
     *
     * @param user     user 实体
     * @param password 明文密码
     */
    @Override
    @Transactional
    public void setUserPassword(User user, String password) {
        if (user == null) {
            throw new UserNotFoundException("got empty user!");
        }
        long uid = user.getId();
        
        // 未设置密码的将其密码设置为空
        String passwordHash;
        if (StringUtil.empty(password)) {
            passwordHash = null;
        } else {
            passwordHash = encodeHash(password);
        }
        // 先尝试查询已有的账号
        List<Account> accounts = this.getAllAccountInfo(uid);
        if (accounts == null || accounts.size() == 0) {
            // 新建登陆账号信息
            accounts = Arrays.asList(
                    Account.fromLoginName(uid, user.getLoginName(), passwordHash),
                    Account.fromPhone(uid, user.getPhone(), passwordHash),
                    Account.fromEmail(uid, user.getEmail(), passwordHash)
            );
            accounts.stream()
                    //过滤空的账号(未设置的)
                    .filter(account -> StringUtil.notEmpty(account.getAccount()))
                    .forEach(account -> {
                        log.info("changing: {}", account);
                        account.createBy(uid);
                        accountMapper.insert(account);
                    });
        } else {
            // 修改已有账号信息
            accounts.forEach(account -> {
                log.info("changing: {}", account);
                account.setToken(passwordHash);
                account.updateBy(uid);
                accountMapper.updateToken(account);
                //accountMapper.update(account, new QueryWrapper<Account>().eq("account", account.getAccount()));
            });
            
        }
    }
    
    
    /**
     * 登录并创建Token
     *
     * @param loginRequest 登录请求
     * @return Token
     */
    @Override
    public TokenUidDTO loginAndCreateToken(LoginDTO loginRequest) {
        Account accountInfo = accountMapper.getOneByAccount(loginRequest.getAccount());
        if (accountInfo == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        dev.chenjr.attendance.dao.entity.User user = userService.getUserByAccount(loginRequest.getAccount());
        if (accountInfo.getLocked()) {
            throw new BadCredentialsException("User is forbidden to login");
        }
        String smsCode = loginRequest.getSmsCode();
        if (smsCode == null && loginRequest.getPassword() == null) {
            throw new BadCredentialsException("must provide password or sms code");
        }
        if (smsCode != null && !smsService.codeValid(loginRequest.getAccount(), ISmsService.TYPE_LOGIN, smsCode)) {
            throw new BadCredentialsException("短信验证码不匹配!");
        }
        if (loginRequest.getPassword() != null && !this.checkPasswordHash(accountInfo.getToken(), loginRequest.getPassword())) {
            throw new BadCredentialsException("用户名或密码不匹配.");
        }
        String token = createToken(user);
        return new TokenUidDTO(token, user.getId());
        
    }
    
    @Override
    public String createToken(User user) {
        // TODO 获取用户角色信息
        return jwtTokenUtil.generateToken(user);
    }
    
    /**
     * 创建Token
     *
     * @param user     用户实体
     * @param longTerm 是否为长期有效的token
     * @return 成功返回token，失败返回null
     */
    @Override
    public String createToken(User user, boolean longTerm) {
        if (!longTerm) {
            return this.createToken(user);
        }
        return jwtTokenUtil.generateLongTermToken(user);
    }
    
    @Override
    public boolean checkPasswordHash(String encodedPassword, String rawPassword) {
        
        log.info("encoded: " + encodedPassword + " raw: " + rawPassword);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    @Override
    public Account getAccountInfo(long uid, String type) {
        return null;
    }
    
    @Override
    public boolean accountExists(long accountId) {
        return this.accountMapper.exists(accountId).orElse(false);
    }
    
    @Override
    public boolean accountExists(String account) {
        return this.accountMapper.existsByAccount(account) != null;
    }
    
    @Override
    public User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        if (principal instanceof dev.chenjr.attendance.dao.entity.User) {
            return (User) principal;
        }
        throw new AuthenticationCredentialsNotFoundException("Cannot found current user!");
        
    }
    
    @Override
    public void bindThirdParty(BindThirdPartyDTO thirdPartyDTO) {
        log.info("{}", thirdPartyDTO);
        boolean exists = this.accountExists(thirdPartyDTO.getAccessToken());
        if (exists) {
            throw new SuperException("用户已绑定其他账号，请先解绑！");
        }
        Account externalAccount = Account
                .external(thirdPartyDTO.getUid(),
                        thirdPartyDTO.getType(),
                        thirdPartyDTO.getOpenid(),
                        thirdPartyDTO.getAccessToken());
        accountMapper.insert(externalAccount);
        
    }
    
    
}
