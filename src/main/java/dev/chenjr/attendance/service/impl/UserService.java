package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.UserMapper;
import dev.chenjr.attendance.entity.User;
import dev.chenjr.attendance.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.chenjr.attendance.service.impl.AuthenticationService.Roles.isValidRoles;

@Service
public class UserService extends BaseService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserById(long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userMapper.getByEmail(email);
        if (user == null) {
            this.getLogger().debug("User not found by email.");
        }
        return user;
    }

    @Override
    public User getUserByPhone(String phone) {
        User user = userMapper.getByPhone(phone);
        if (user == null) {
            this.getLogger().info("User not found by phone.");
        }
        return user;
    }

    @Override
    public User getUserByLoginName(String loginName) {
        User user = userMapper.getByLoginName(loginName);
        if (user == null) {
            this.getLogger().info("User not found by loginName.");
        }
        return user;
    }

    @Override
    public User getUserByAccount(String account) {
        User user = getUserByPhone(account);
        if (user == null) {
            user = getUserByEmail(account);
        }

        return user;
    }

    @Override
    public boolean checkPasswordHash(String encodedPassword, String rawPassword) {

        this.getLogger().info("encoded: " + encodedPassword + " raw: " + rawPassword);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    @Override
    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
        return userMapper.getAll((pageIndex - 1) * pageSize, pageSize);
    }

    @Override
    @Transactional
    public User register(String name, String email, String phone, String roles) {
        User user = new User();
        user.setEmail(email);
        user.setRealName(name);
        user.setPhone(phone);
        if (isValidRoles(roles)) {
            user.setRoles(roles);
        }
        userMapper.insert(user);
        return user;
    }

    @Override
    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userMapper.update(user);
    }


    @Override
    public boolean userExists(long uid) {
        return userMapper.idExists(uid) != null;
    }

    @Override
    public boolean userExists(String account) {
        Boolean exists;
        exists = userMapper.phoneExists(account);
        if (exists == null) {
            exists = userMapper.emailExists(account);
        }
        return exists != null;
    }
}
