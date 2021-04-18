package dev.chenjr.attendance.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService extends BaseService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

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
            log.error("User not found by loginName.");
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
    public List<UserInfoResponse> getUsers(long pageIndex, long pageSize) {
        Page<User> userPage = new Page<>(pageIndex, pageSize);
        List<User> records = userMapper.selectPage(userPage, null).getRecords();
        Stream<UserInfoResponse> infoResponseStream = records.stream().map(this::userToUserInfo);
        return infoResponseStream.collect(Collectors.toList());
    }

    private UserInfoResponse userToUserInfo(User user) {
        UserInfoResponse userInfo = new UserInfoResponse(
                user.getLoginName(),
                user.getRealName(),
                "UNKNOWN",
                user.getEmail(),
                user.getPhone(),
                user.getAcademicId(),
                0L, "UNKNOWN");
        // TODO 改到字典类中查询
        HashMap<Integer, String> genderMap = new HashMap<>();
        genderMap.put(0, "未知");
        genderMap.put(1, "男");
        genderMap.put(2, "女");
        userInfo.setGender(genderMap.getOrDefault(user.getGender(), "NOT_FOUND"));
        return userInfo;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setRealName(request.getRealName());
        user.setLoginName(request.getLoginName());
        user.setPhone(request.getPhone());
        user.setGender(0);
        if (user.getLoginName() == null || "".equals(user.getLoginName())) {
            return null;
        }
        // TODO 设置role
        int inserted = userMapper.insert(user);
        if (inserted != 1) {
            // TODO 抛出异常
            log.error("Fail to insert user!");
            return null;
        }
        accountService.setUserPassword(user, request.getPassword());
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
