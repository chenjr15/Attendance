package dev.chenjr.attendance.service;


import dev.chenjr.attendance.dao.entity.Account;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.SetPasswordFailException;
import dev.chenjr.attendance.service.dto.InputBindThirdPartyDTO;
import dev.chenjr.attendance.service.dto.InputLoginDTO;
import dev.chenjr.attendance.service.dto.TokenUidDTO;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 认证(Authentication)服务，处理账号相关的东西，账号密码校验、登录方式封禁、密码修改、Token等
 */
public interface IAccountService extends IService {
    boolean checkPasswordAndAccount(String account, String rawPassword);

    /**
     * 为注册的用户设置用户密码
     *
     * @param user        用户实例
     * @param rawPassword 未加密的原始密码
     * @return 是否成功
     */
    boolean registerAccount(User user, String rawPassword);

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
     * @return 成功与否
     */
    boolean setUserPassword(long uid, String password);


    /**
     * 设置(修改)用户密码
     * 注册的时候用这个方法会新建所有的Account
     *
     * @param user     user 实体
     * @param password 明文密码
     * @return 成功与否
     */
    boolean setUserPassword(User user, String password);

    void setUserPasswordWithSmsCode(@NotNull User user, String password, String code) throws SetPasswordFailException;

    /**
     * 登陆并创建用户Token
     *
     * @param loginRequest 登陆请求
     * @return token
     */
    TokenUidDTO loginAndCreateToken(InputLoginDTO loginRequest);

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
    void bindThirdParty(InputBindThirdPartyDTO thirdPartyDTO);
}
