package dev.chenjr.attendance.service;


import dev.chenjr.attendance.dao.entity.Account;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.SetPasswordFailException;
import dev.chenjr.attendance.service.dto.BindThirdPartyDTO;
import dev.chenjr.attendance.service.dto.LoginDTO;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.TokenUidDTO;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 认证(Authentication)服务，处理账号相关的东西，账号密码校验、登录方式封禁、密码修改、Token等
 */
public interface IAccountService extends IService {
    boolean checkPasswordAndAccount(String account, String rawPassword);
    
    
    /**
     * 校验密码是否匹配
     *
     * @param encodedPassword 加密后的hash
     * @param rawPassword     明文密码
     * @return 是否匹配
     */
    boolean checkPasswordHash(String encodedPassword, String rawPassword);
    
    /**
     * 获取用户的账号信息
     *
     * @param uid  user id
     * @param type 账号类型
     * @return 指定的账号信息
     */
    Account getAccountInfo(long uid, String type);
    
    /**
     * 获取用户的手机账号信息
     *
     * @param uid user id
     * @return 指定的手机账号信息
     */
    Account getOneAccountInfo(long uid);
    
    /**
     * 获取用户所有的Account
     *
     * @param uid user id
     * @return Account 列表
     */
    List<Account> getAllAccountInfo(long uid);
    
    /**
     * 设置(修改)用户密码
     * 注册的时候用这个方法会新建所有的Account
     *
     * @param uid      user id
     * @param password 明文密码
     */
    void setUserPassword(long uid, String password);
    
    
    /**
     * 设置(修改)用户密码
     * 注册的时候用这个方法会新建所有的Account
     *
     * @param user     user 实体
     * @param password 明文密码
     */
    void setUserPassword(User user, String password);
    
    /**
     * 创建用户的Account
     * 注册的时候用这个方法会新建的Account
     *
     * @param user user 实体
     */
    void setUserPassword(User user);
    
    void setUserPasswordWithSmsCode(@NotNull User user, String password, String code) throws SetPasswordFailException;
    
    /**
     * 登陆并创建用户Token
     *
     * @param loginRequest 登陆请求
     * @return token
     */
    TokenUidDTO loginAndCreateToken(LoginDTO loginRequest);
    
    /**
     * 创建Token
     *
     * @param user 用户实体
     * @return 成功返回token，失败返回null
     */
    String createToken(User user);
    
    /**
     * 创建Token
     *
     * @param user     用户实体
     * @param longTerm 是否为长期有效的token
     * @return 成功返回token，失败返回null
     */
    String createToken(User user, boolean longTerm);
    
    /**
     * 判断账号是否存在，通过id判断
     *
     * @param accountId 账号id
     * @return 是否存在
     */
    boolean accountExists(long accountId);
    
    /**
     * 判断账号是否存在，通过账号判断
     *
     * @param account 账号
     * @return 是否存在
     */
    boolean accountExists(String account);
    
    User currentUser();
    
    /**
     * 绑定第三方账号
     *
     * @param thirdPartyDTO 绑定信息
     */
    void bindThirdParty(BindThirdPartyDTO thirdPartyDTO);
    
    /**
     * 注册
     * 1. 不检查验证码
     * 2. 密码设置
     * 3. 创建用户
     * 4. 创建Account
     * 5. 创建角色关系
     *
     * @param request 注册信息
     * @return token和uid
     */
    TokenUidDTO register(RegisterRequest request);
    
    /**
     * 初始化用户，创建account
     */
    void initUser(long uid);
}
