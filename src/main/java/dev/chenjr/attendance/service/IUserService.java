package dev.chenjr.attendance.service;


import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.UserDTO;
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
    UserDTO userToUserInfo(User user);

    @Transactional
    User register(RegisterRequest request);

    User register(User user);

    @Transactional
    void updateUser(User user);

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
}
