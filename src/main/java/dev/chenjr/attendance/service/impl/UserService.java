package dev.chenjr.attendance.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.*;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.UserNotFoundException;
import dev.chenjr.attendance.service.IStorageService;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    DictionaryService dictionaryService;
    
    @Autowired
    IStorageService storageService;
    
    @Autowired
    AccountService accountService;
    @Autowired
    RoleService roleService;
    
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserCourseMapper userCourseMapper;
    @Autowired
    CheckInLogMapper checkInLogMapper;
    
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
            log.error("User not found by phone.");
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
    @Transactional
    public User createUser(RegisterRequest request) {
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRealName(request.getRealName());
        user.setLoginName(request.getLoginName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        
        this.createUser(user);
        
        return user;
    }
    
    @Override
    public User createUser(User user) {
        userMapper.insert(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public UserDTO createAndInitUser(UserDTO dto) {
        User user = dto2user(dto);
        user = this.createUser(user);
        dto.setId(user.getId());
        accountService.initUser(dto.getId());
        if (dto.getRoles() == null || dto.getRoles().size() == 0) {
            
            roleService.initUser(dto.getId());
        } else {
            List<Long> roleId = dto.getRoles().stream().map(RoleDTO::getId).collect(Collectors.toList());
            roleService.setUserRoles(dto.getId(), roleId);
        }
        return getUser(user.getId());
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
        if (exists == null) {
            exists = userMapper.loginNameExists(account);
        }
        
        return exists != null;
    }
    
    @Override
    public void deleteByUid(long uid) {
        accountMapper.deleteByUid(uid);
        userRoleMapper.removeAllRole(uid);
        userCourseMapper.deleteByUser(uid);
        checkInLogMapper.deleteByUser(uid);
        userMapper.deleteById(uid);
    }
    
    
    @Override
    public String modifyAvatar(Long uid, MultipartFile uploaded) {
        Optional<Boolean> exists = this.userMapper.exists(uid);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        String storeName = storageService.storeFile(uploaded);
        User user = new User();
        user.setId(uid);
        user.setAvatar(storeName);
        user.updateBy(uid);
        userMapper.updateById(user);
        return storageService.getFullUrl(storeName);
    }
    
    /**
     * @param uid 用户id
     * @return 用户信息
     */
    @Override
    public UserDTO getUser(Long uid) {
        User user = userMapper.selectById(uid);
        if (user == null) {
            throw HttpStatusException.notFound("用户id不存在！");
        }
        return user2dto(user);
    }
    
    /**
     * 分页返回用户，同时支持筛选排序
     *
     * @param pageSort 分页排序筛选数据
     * @return 分页后的数据
     */
    @Override
    public PageWrapper<UserDTO> listUser(PageSort pageSort) {
        Page<User> page = pageSort.getPage();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw = pageSort.buildQueryWrapper(qw, "realName");
        page = userMapper.selectPage(page, qw);
        List<UserDTO> collected = page.getRecords().stream().map(this::user2dto).collect(Collectors.toList());
        return PageWrapper.fromList(page, collected);
    }
    
    /**
     * 修改用户信息
     *
     * @param desiredDto 想修改的数据
     * @return 修改后的数据
     */
    @Override
    public UserDTO modifyUser(UserDTO desiredDto) {
        if (desiredDto == null) {
            return null;
        }
        User user = userMapper.selectById(desiredDto.getId());
        if (user == null) {
            throw HttpStatusException.notFound();
        }
        User desiredUser = new User();
        desiredUser.setId(desiredDto.getId());
        //desiredUser.setPhone(desiredDto.getPhone());
        desiredUser.setRealName(desiredDto.getRealName());
        desiredUser.setGender(desiredDto.getGenderValue());
        desiredUser.setSchoolMajor(desiredDto.getSchoolMajorID());
        desiredUser.setAcademicId(desiredDto.getAcademicId());
        
        userMapper.updateById(desiredUser);
        return getUser(desiredUser.getId());
    }
    
    /**
     * 通过id 查询用户的名字
     *
     * @param id 用户id
     * @return 用户名
     */
    @Override
    public String getRealNameById(Long id) {
        if (id == null) {
            return null;
        }
        return userMapper.getRealNameById(id);
    }
    
    
    /**
     * 获取学生简介信息
     *
     * @param uid uid
     * @return 学生简介信息
     */
    @Override
    public CourseStudentDTO getStudent(Long uid) {
        User stu = userMapper.getStudent(uid);
        if (stu == null) {
            return null;
        }
        return new CourseStudentDTO(stu.getId(), stu.getRealName(), stu.getAcademicId(), 0);
    }
    
    /**
     * 将用户实体对象转成DTO，尽可能补全数据
     *
     * @param user 实体对象
     * @return DTO对象
     */
    @Override
    public UserDTO user2dto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAcademicId(user.getAcademicId());
        dto.setLoginName(user.getLoginName());
        dto.setRealName(user.getRealName());
        Long schoolMajor = user.getSchoolMajor();
        if (schoolMajor != null) {
            dto.setSchoolMajorID(schoolMajor);
            String orgName = organizationService.getOrgName(schoolMajor);
            dto.setSchoolMajorName(orgName);
        }
        
        String avatar = user.getAvatar();
        String avatarUrl = storageService.getFullUrl(avatar);
        dto.setAvatar(avatarUrl);
        
        String sexName = dictionaryService.getCacheDictDetail("sex", user.getGender(), "未知");
        dto.setGender(sexName);
        dto.setGenderValue(user.getGender());
        List<RoleDTO> userRole = roleService.getUserRole(user.getId());
        dto.setRoles(userRole);
        
        return dto;
    }
    
    private User dto2user(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setAcademicId(dto.getAcademicId());
        user.setSchoolMajor(dto.getSchoolMajorID());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRealName(dto.getRealName());
        user.setLoginName(dto.getLoginName());
        user.setGender(dto.getGenderValue());
        return user;
    }
}
