package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.entity.Role;
import dev.chenjr.attendance.dao.entity.UserRole;
import dev.chenjr.attendance.dao.mapper.RoleMapper;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.dao.mapper.UserRoleMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IRoleService;
import dev.chenjr.attendance.service.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    
    @Autowired
    UserMapper userMapper;
    
    /**
     * 列出所有角色
     *
     * @return 角色权限
     */
    @Override
    public List<RoleDTO> listRole() {
        List<Role> roles = roleMapper.selectList(null);
        return roles.stream().map(this::role2dto).collect(Collectors.toList());
    }
    
    
    /**
     * 创建角色
     *
     * @param dto 创建的信息
     * @return 创建出来的结果
     */
    @Override
    public RoleDTO createRole(RoleDTO dto) {
        Role role = dto2role(dto);
        roleMapper.insert(role);
        return getRole(role.getId());
    }
    
    
    /**
     * 修改角色
     *
     * @param dto 修改的信息
     * @return 修改后的结果
     */
    @Override
    public RoleDTO modifyRole(RoleDTO dto) {
        Optional<Boolean> exists = roleMapper.exists(dto.getId());
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        Role role = dto2role(dto);
        roleMapper.updateById(role);
        
        return getRole(dto.getId());
        
    }
    
    /**
     * 删除角色
     *
     * @param roleId role id
     */
    @Override
    public void deleteRole(long roleId) {
        Optional<Boolean> exists = this.roleMapper.exists(roleId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        roleMapper.deleteById(roleId);
    }
    
    /**
     * 获取角色信息
     *
     * @param roleId 角色id
     * @return 角色信息
     */
    @Override
    public RoleDTO getRole(long roleId) {
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw HttpStatusException.notFound();
        }
        return role2dto(role);
    }
    
    /**
     * 获取某个用户的角色
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    @Override
    public List<RoleDTO> getUserRole(long userId) {
        Optional<Boolean> exists = userMapper.exists(userId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("找不到用户!");
        }
        List<Role> roles = userRoleMapper.getUserRole(userId);
        return roles.stream().map(this::role2dto).collect(Collectors.toList());
    }
    
    /**
     * 给用户添加角色
     *
     * @param userId 用户id
     * @param roleId 要添加的角色id
     * @return 用户所有的角色列表
     */
    @Override
    public List<RoleDTO> addRoleToUser(long userId, long roleId) {
        Optional<Boolean> exists = roleMapper.exists(roleId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("找不到角色！");
        }
        exists = userMapper.exists(userId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("找不到用户！");
        }
        exists = userRoleMapper.existsRelation(roleId, userId);
        if (exists.orElse(false)) {
            throw HttpStatusException.conflict("重复添加！");
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
        return getUserRole(userId);
    }
    
    /**
     * 给用户设置单一角色，会替换原有的角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return 用户角色列表
     */
    @Override
    @Transactional
    public List<RoleDTO> setUserSingleRole(long userId, long roleId) {
        userRoleMapper.removeAllRole(userId);
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
        return getUserRole(userId);
    }
    
    /**
     * 给用户去掉角色
     *
     * @param userId 用户id
     * @param roleId 要去掉的角色id
     * @return 用户所有的角色列表
     */
    @Override
    public List<RoleDTO> removeRoleOfUser(long userId, long roleId) {
        Optional<Boolean> exists = userRoleMapper.existsRelation(roleId, userId);
        if (exists.orElse(false)) {
            throw HttpStatusException.notFound("该用户没有该角色！");
        }
        userRoleMapper.deleteRelation(roleId, userId);
        
        return getUserRole(userId);
    }
    
    private RoleDTO role2dto(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setCode(role.getCode());
        dto.setName(role.getName());
        return dto;
    }
    
    private Role dto2role(RoleDTO dto) {
        Role role = new Role();
        role.setCode(dto.getCode());
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }
}
