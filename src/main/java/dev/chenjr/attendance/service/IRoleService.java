package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.RoleDTO;

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
    List<RoleDTO> addRoleToUser(long userId, long roleId);
    
    /**
     * 给用户去掉角色
     *
     * @param userId 用户id
     * @param roleId 要去掉的角色id
     * @return 用户所有的角色列表
     */
    List<RoleDTO> removeRoleToUser(long userId, long roleId);
}
