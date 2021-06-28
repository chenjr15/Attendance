package dev.chenjr.attendance.service;


import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    
    /**
     * 将用户实体对象转成DTO，尽可能补全数据
     *
     * @param user 实体对象
     * @return DTO对象
     */
    UserDTO user2dto(User user);
    
    /**
     * 创建用户，只修改user表相关信息
     *
     * @param request 注册信息
     * @return 插入的数据
     */
    @Transactional
    User createUser(RegisterRequest request);
    
    /**
     * 单纯的user insert
     *
     * @param user 要插入的user
     * @return 创建的用户信息
     */
    User createUser(User user);
    
    /**
     * 创建用户并用默认信息初始化 适用于管理员添加的情况
     *
     * @param toCreate 待创建用户信息
     * @return 创建成功的信息
     */
    UserDTO createAndInitUser(UserDTO toCreate);
    
    boolean userExists(long uid);
    
    boolean userExists(String account);
    
    void deleteByUid(long uid);
    
    String modifyAvatar(Long uid, MultipartFile uploaded);
    
    /**
     * @param uid 用户id
     * @return 用户信息
     */
    UserDTO getUser(Long uid);
    
    /**
     * 分页返回用户，同时支持筛选排序
     *
     * @param pageSort 分页排序筛选数据
     * @return 分页后的数据
     */
    PageWrapper<UserDTO> listUser(PageSort pageSort);
    
    /**
     * 修改用户信息
     *
     * @param desiredDto 想修改的数据
     * @return 修改后的数据
     */
    UserDTO modifyUser(UserDTO desiredDto);
    
    /**
     * 通过id 查询用户的名字
     *
     * @param id 用户id
     * @return 用户名
     */
    String getRealNameById(Long id);
    
    /**
     * 获取学生简介信息
     *
     * @param uid uid
     * @return 学生简介信息
     */
    CourseStudentDTO getStudent(Long uid);
    
    
}
