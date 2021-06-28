package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.*;

import java.util.Collection;
import java.util.List;

/**
 * 角色服务接口，角色相关业务的支持
 */
public interface IRoleService extends IService {
    
    /**
     * 列出所有角色
     *
     * @return 角色权限
     */
    List<RoleDTO> listRole();
    
    /**
     * 创建角色
     *
     * @param toCreate 创建的信息
     * @return 创建出来的结果
     */
    RoleDTO createRole(RoleDTO toCreate);
    
    /**
     * 修改角色
     *
     * @param toModify 修改的信息
     * @return 修改后的结果
     */
    RoleDTO modifyRole(RoleDTO toModify);
    
    /**
     * 删除角色
     *
     * @param roleId role id
     */
    void deleteRole(long roleId);
    
    /**
     * 获取角色信息
     *
     * @param roleId 角色id
     * @return 角色信息
     */
    RoleDTO getRole(long roleId);
    
    /**
     * 获取某个用户的角色
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    List<RoleDTO> getUserRole(long userId);
    
    /**
     * 给用户添加角色
     *
     * @param userId 用户id
     * @param roleId 要添加的角色id
     * @return 用户所有的角色列表
     */
    List<RoleDTO> addRoleByCodeToUser(long userId, long roleId);
    
    /**
     * 给用户添加角色
     *
     * @param userId   用户id
     * @param roleCode 要添加的角色编码
     */
    void addRoleByCodeToUser(long userId, String roleCode);
    
    /**
     * 给用户去掉角色
     *
     * @param userId 用户id
     * @param roleId 要去掉的角色id
     * @return 用户所有的角色列表
     */
    List<RoleDTO> removeRoleOfUser(long userId, long roleId);
    
    /**
     * 给用户设置单一角色，会替换原有的角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 用户角色列表
     */
    List<RoleDTO> setUserSingleRole(long userId, long roleId);
    
    /**
     * 初始化用户信息(设置默认角色)
     *
     * @param id 用户id
     */
    void initUser(Long id);
    
    /**
     * 获取角色下的所有用户
     *
     * @param roleId   角色id
     * @param pageSort 分页
     * @return 用户列表
     */
    PageWrapper<UserDTO> getRoleUsers(long roleId, PageSort pageSort);
    
    /**
     * 获取角色下的所有菜单
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    PageWrapper<MenuDTO> getRoleMenus(long roleId);
    
    /**
     * 给某个角色增加菜单访问权限
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    void addMenuToRole(long roleId, long menuId);
    
    /**
     * 给某个角色批量增加菜单访问权限
     *
     * @param roleId 角色id
     * @param menuId 菜单id列表
     */
    void addMenuToRole(long roleId, List<Long> menuId);
    
    /**
     * 批量删除角色可以访问的菜单
     *
     * @param roleId   角色id
     * @param menuList 菜单id列表
     */
    void removeMenuToRole(long roleId, List<Long> menuList);
    
    /**
     * 删除角色的菜单访问权限
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    void removeMenuToRole(long roleId, long menuId);
    
    Collection<Long> getUserMenuId(Long id);
    
    /**
     * 完全修改某个角色的菜单权限(所给即所得)
     *
     * @param roleId   角色id
     * @param menuList 菜单id列表
     */
    void setRoleMenus(long roleId, List<Long> menuList);
}
