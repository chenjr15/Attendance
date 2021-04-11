package dev.chenjr.attendance.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserService extends BaseService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserById(long id) {

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userMapper.getByEmail(email);
        if (user == null) {
            log.debug("User not found by email.");
        }
        return user;
    }

    @Override
    public User getUserByPhone(String phone) {
        User user = userMapper.getByPhone(phone);
        if (user == null) {
            log.info("User not found by phone.");
        }
        return user;
    }

    @Override
    public User getUserByLoginName(String loginName) {
        User user = userMapper.getByLoginName(loginName);
        if (user == null) {
            log.info("User not found by loginName.");
        }
        return user;
    }

    @Override
    public User getUserByAccount(String account) {
        User user = getUserByPhone(account);
        if (user == null) {
            user = getUserByEmail(account);
        }
        if (user == null) {
            user = getUserByLoginName(account);
        }

        return user;
    }


    @Override
    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
        Page<User> userPage = new Page<>(pageIndex, 10);
        return userMapper.selectPage(userPage, null).getRecords();
    }

    @Override
    @Transactional
    public User register(String name, String email, String phone, String roles) {
        User user = new User();
        user.setEmail(email);
        user.setRealName(name);
        user.setPhone(phone);
        // TODO 设置role
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
        userMapper.update(user, null);
    }


    @Override
    public boolean userExists(long uid) {
        return userMapper.exists(uid) != null;
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
