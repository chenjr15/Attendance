package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.old.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    boolean checkPasswordHash(String encodedPassword, String rawPassword);

    List<User> getUsers(int pageIndex);

    @Transactional
    User register(String name, String email, String phone, String roles);

    User register(User user);

    @Transactional
    void updateUser(User user);

    boolean userExists(long uid);

    boolean userExists(String account);
}
