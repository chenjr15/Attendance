package dev.chenjr.attendance.service;


import dev.chenjr.attendance.dao.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户相关业务支持服务
 */
public interface IUserService extends IService {
    User getUserById(long id);

    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    User getUserByLoginName(String loginName);

    /**
     * Email/Phone/LoginName
     *
     * @param account Email/Phone/LoginName
     * @return User entity
     */
    User getUserByAccount(String account);


    List<User> getUsers(int pageIndex);

    @Transactional
    User register(String name, String email, String phone, String roles);

    User register(User user);

    @Transactional
    void updateUser(User user);

    boolean userExists(long uid);

    boolean userExists(String account);
}
