package dev.chenjr.attendance.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.AccountMapper;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.RegisterException;
import dev.chenjr.attendance.exception.UserNotFoundException;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.chenjr.attendance.utils.RandomUtil.randomStringWithDate;
import static org.springframework.util.StringUtils.getFilenameExtension;

@Service
@Slf4j
public class UserService extends BaseService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @Override
    public User getUserById(long id) {

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found by id.");
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
    public List<UserDTO> getUsers(long pageIndex, long pageSize) {
        Page<User> userPage = new Page<>(pageIndex, pageSize);
        userMapper.selectPage(userPage, null);
        List<User> records = userPage.getRecords();
        Stream<UserDTO> infoResponseStream = records.stream().map(this::userToUserInfo);
        return infoResponseStream.collect(Collectors.toList());
    }

    @Override
    public UserDTO userToUserInfo(User user) {
        UserDTO userInfo = user2DTO(user);
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
//        if (user.getLoginName() == null || "".equals(user.getLoginName())) {
//            return null;
//        }
        // TODO 设置role
        int inserted = userMapper.insert(user);
        if (inserted != 1) {
            log.error("Fail to insert user!" + inserted);
            throw new RegisterException("db fail!");
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
        userMapper.updateById(user);
    }


    @Override
    public boolean userExists(long uid) {
        return userMapper.exists(uid).orElse(false);
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

    @Override
    public void deleteByUid(long uid) {
        accountMapper.deleteByUid(uid);
        userMapper.deleteById(uid);
    }

    @Value("${avatar.storage.path}")
    String avatarStoragePath;

    @Override
    public String modifyAvatar(Long uid, MultipartFile uploaded) {
        Optional<Boolean> exists = this.userMapper.exists(uid);
        log.info("EXISTS : {}", exists);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        String storeName = randomStringWithDate(20);
        String extension = getFilenameExtension(uploaded.getOriginalFilename());
        storeName = storeName + '.' + extension;
        File saveFile = new File(avatarStoragePath + storeName);
        try {
            uploaded.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw HttpStatusException.badRequest("上传失败！");
        }
        User user = new User();
        user.setId(uid);
        user.setAvatar(storeName);
        userMapper.updateById(user);
        return storeName;
    }

    UserDTO user2DTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAcademicId(user.getAcademicId());
        dto.setLoginName(user.getLoginName());
        dto.setRealName(user.getRealName());
        dto.setSchoolMajorID(user.getSchoolMajor());
        dto.setAvatar(user.getAvatar());
        return dto;
    }
}
