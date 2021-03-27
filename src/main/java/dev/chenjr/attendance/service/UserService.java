package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.UserMapper;
import dev.chenjr.attendance.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService extends BaseService {
    @Autowired
    UserMapper userMapper;

    public User getUserById(long id) {
        User user = userMapper.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userMapper.getByEmail(email);
        if (user == null) {
            this.getLogger().debug("User not found by email.");
        }
        return user;
    }

    public User getUserByPhone(String phone) {
        User user = userMapper.getByPhone(phone);
        if (user == null) {
            this.getLogger().info("User not found by phone.");
        }
        return user;
    }

    public User getUserByAccount(String account) {
        User user = getUserByPhone(account);
        if (user == null) {
            user = getUserByEmail(account);
        }
        return user;
    }


    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
        return userMapper.getAll((pageIndex - 1) * pageSize, pageSize);
    }

    @Transactional
    public User register(String name, String email, String phone) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        userMapper.insert(user);
        return user;
    }

    public User register(User user) {
        userMapper.insert(user);
        return user;
    }

    @Transactional
    public void updateUser(User user) {
        userMapper.update(user);
    }


    public boolean userExists(long uid) {
        return userMapper.idExists(uid) != null;
    }

    public boolean userExists(String account) {
        Boolean exists;
        exists = userMapper.phoneExists(account);
        if (exists == null) {
            exists = userMapper.emailExists(account);
        }
        return exists != null;
    }
}
