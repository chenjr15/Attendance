package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.AccountInfoMapper;
import dev.chenjr.attendance.dao.entity.AccountInfo;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.LoginRequest;
import dev.chenjr.attendance.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Authentication Service 认证服务，处理身份认证(登入登出，用户密码校验，Token校验，权限分配等)
 */
@Service
public class AuthenticationService extends BaseService {


    @Autowired
    UserService userService;
    @Autowired
    AccountInfoMapper accountInfoMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 检验账号和密码是否匹配
     *
     * @param account     邮箱或者手机号
     * @param rawPassword 未加密的密码
     * @return 是否匹配
     */
    public boolean checkPasswordAndAccount(String account, String rawPassword) {
        this.getLogger().info("Checking: " + account + " with " + rawPassword);
        User user = userService.getUserByAccount(account);
        if (user == null) {
            this.getLogger().info("User not found!");
            return false;
        }
        AccountInfo accountInfo;
        accountInfo = accountInfoMapper.getByUid(user.getId());
        if (accountInfo == null) {
            this.getLogger().info("localAuth not found!");

            return false;
        }
        String encoded = accountInfo.getPassword();
        return userService.checkPasswordHash(encoded, rawPassword);
    }

    public void setUserRole(User user, String role) {
        if (Roles.isValidRole(role)) {
            Set<String> roles = StringUtils.commaDelimitedListToSet(user.getRoles());
            roles.add(role);
            user.setRoles(StringUtils.collectionToCommaDelimitedString(roles));
        }
        this.getLogger().error("Not a valid role: " + role);
    }

    public void unsetUserRole(User user, String role) {
        if (Roles.isValidRole(role)) {
            Set<String> roles = StringUtils.commaDelimitedListToSet(user.getRoles());
            roles.remove(role);
            user.setRoles(StringUtils.collectionToCommaDelimitedString(roles));
        }
        this.getLogger().error("Not a valid role: " + role);
    }

    private String encodeHash(String password) {

        return passwordEncoder.encode(password);
    }

    public void changePassword(long uid, String password) {
        this.setAuth(uid, password);
    }

    public AccountInfo getAuth(long uid) {
        return this.accountInfoMapper.getByUid(uid);
    }


    public boolean setAuth(long uid, String password) {

        boolean exists = userService.userExists(uid);
        if (!exists) {
            return false;
        }

        String passwordHash = encodeHash(password);
        boolean matches = passwordEncoder.matches(password, passwordHash);
        this.getLogger().info(String.format("setting password for %d to %s, matched:%b", uid, password, matches));
        AccountInfo accountInfo = this.getAuth(uid);
        if (accountInfo == null) {
            // 新建密码登陆信息
            accountInfo = new AccountInfo(uid, passwordHash);
            accountInfoMapper.insert(accountInfo);
        } else {
            accountInfo.setPassword(passwordHash);
            accountInfoMapper.update(accountInfo);
        }
        return true;
    }


    /**
     * 登录并创建Token
     *
     * @param loginRequest 登录请求
     * @return Token
     */
    public String createLoginToken(LoginRequest loginRequest) {
        User user = userService.getUserByAccount(loginRequest.getAccount());
        // TODO 减少数据库查询 只用一次AccountInfo和User
        if (!this.checkPasswordAndAccount(loginRequest.getAccount(), loginRequest.getPassword())) {
            throw new BadCredentialsException("The user name or password is not correct.");
        }
        AccountInfo accountInfo = getAuth(user.getId());
        if (accountInfo.isLocked()) {
            throw new BadCredentialsException("User is forbidden to login");
        }
        UserDetails details = new MyUserDetail(user);
        return jwtTokenUtil.generateToken(details);
    }

//    /**
//     * 登录认证换取JWT令牌
//     *
//     * @param account
//     * @param password
//     * @return
//     */
//    public String login(String account, String password) {
//        //用户验证
//        Authentication authentication = null;
//        try {
//            // 进行身份验证,
//            authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(account, password));
//        } catch (Exception e) {
//            throw new RuntimeException("用户名密码错误");
//        }
//
//        UserDetail loginUser = (UserDetail) authentication.getPrincipal();
//        // 生成token
//        return jwtTokenUtil.generateToken(loginUser);
//
//    }

    public static final class Roles {
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_TEACHER = "ROLE_TEACHER";
        public static final String ROLE_STUDENT = "ROLE_STUDENT";
        public static final HashSet<String> allRoles = new HashSet<>(Arrays.asList(ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN));

        public static boolean isValidRole(String role) {
            return allRoles.contains(role);
        }

        public static boolean isValidRoles(String roles) {
            if (roles != null) {
                return allRoles.containsAll(Arrays.asList(roles.split(",")));
            } else {
                return false;
            }
        }
    }
}
