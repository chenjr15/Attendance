package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Role;
import dev.chenjr.attendance.dao.entity.RoleMenu;
import dev.chenjr.attendance.dao.entity.UserRole;
import dev.chenjr.attendance.dao.mapper.RoleMapper;
import dev.chenjr.attendance.dao.mapper.RoleMenuMapper;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.dao.mapper.UserRoleMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IRoleService;
import dev.chenjr.attendance.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleService implements IRoleService {
    public static final String DEFAULT_ROLE = "STUDENT";
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    
    @Autowired
    UserMapper userMapper;
    
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    
    
    @Override
    public Collection<Long> getUserMenuId(Long uid) {
        List<Long> userRole = userRoleMapper.getUserRole(uid);
        TreeSet<Long> combined = new TreeSet<>();
        for (Long roleId : userRole) {
            List<Long> roleMenus = roleMenuMapper.getRoleMenus(roleId);
            combined.addAll(roleMenus);
        }
        return combined;
    }
    
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
    @Transactional
    public void deleteRole(long roleId) {
        Optional<Boolean> exists = this.roleMapper.exists(roleId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        // 删除相关的用户关系
        userRoleMapper.deleteByRoleId(roleId);
        //删除相关的角色关系
        roleMenuMapper.deleteByRoleId(roleId);
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
        List<Long> roleIds = userRoleMapper.getUserRole(userId);
        if (roleIds.size() == 0) {
            return new ArrayList<>();
        }
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
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
    public List<RoleDTO> addRoleByCodeToUser(long userId, long roleId) {
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
     * 给用户添加角色
     *
     * @param userId   用户id
     * @param roleCode 要添加的角色编码
     */
    @Override
    public void addRoleByCodeToUser(long userId, String roleCode) {
        
        Role role = roleMapper.getRole(roleCode);
        if (role == null) {
            throw HttpStatusException.notFound("找不到角色！");
        }
        Optional<Boolean> exists = userMapper.exists(userId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("找不到用户！");
        }
        exists = userRoleMapper.existsRelation(role.getId(), userId);
        if (exists.orElse(false)) {
            throw HttpStatusException.conflict("重复添加！");
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRoleMapper.insert(userRole);
        
    }
    
    /**
     * 初始化用户信息(设置默认角色)
     *
     * @param id 用户id
     */
    @Override
    public void initUser(Long id) {
        this.addRoleByCodeToUser(id, DEFAULT_ROLE);
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
        userRoleMapper.insert(new UserRole(userId, roleId));
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
        if (!exists.orElse(false)) {
            throw HttpStatusException.notFound("该用户没有该角色！");
        }
        userRoleMapper.deleteRelation(roleId, userId);
        
        return getUserRole(userId);
    }
    
    /**
     * 获取角色下的所有用户
     *
     * @param roleId   角色id
     * @param pageSort 分页
     * @return 用户列表
     */
    @Override
    public PageWrapper<UserDTO> getRoleUsers(long roleId, PageSort pageSort) {
        Page<Long> page = pageSort.getPage();
        page = userRoleMapper.getRoleUsers(roleId, page);
        List<UserDTO> collect = new ArrayList<>(page.getRecords().size());
        page.getRecords().stream().map(userService::getUser).forEachOrdered(collect::add);
        return PageWrapper.fromList(page, collect);
    }
    
    /**
     * 获取角色下的所有可访问的菜单
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    @Override
    public PageWrapper<MenuDTO> getRoleMenus(long roleId) {
        List<Long> permIdList = roleMenuMapper.getRoleMenus(roleId);
        List<MenuDTO> collect = permIdList.stream().map(menuService::getMenu).collect(Collectors.toList());
        return PageWrapper.singlePage(collect);
    }
    
    /**
     * 给某个角色增加菜单访问权限
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    @Override
    public void addMenuToRole(long roleId, long menuId) {
        Boolean exists = roleMenuMapper.exists(roleId, menuId);
        if (exists != null) {
            return;
        }
        roleMenuMapper.insert(new RoleMenu(menuId, roleId));
    }
    
    /**
     * 给某个角色批量增加菜单访问权限
     *
     * @param roleId   角色id
     * @param menuList 菜单id列表
     */
    @Override
    @Transactional
    public void addMenuToRole(long roleId, List<Long> menuList) {
        for (Long menuId : menuList) {
            this.addMenuToRole(roleId, menuId);
        }
    }
    
    /**
     * 批量删除角色可以访问的菜单
     *
     * @param roleId   角色id
     * @param menuList 菜单id列表
     */
    @Override
    @Transactional
    public void removeMenuToRole(long roleId, List<Long> menuList) {
        for (Long menuId : menuList) {
            this.removeMenuToRole(roleId, menuId);
        }
    }
    
    /**
     * 删除角色的菜单访问权限
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    @Override
    public void removeMenuToRole(long roleId, long menuId) {
        roleMenuMapper.delete(roleId, menuId);
    }
    
    /**
     * 完全修改某个角色的菜单权限
     * (所给即所得)
     *
     * @param roleId   角色id
     * @param newMenus 菜单id列表
     */
    @Override
    @Transactional
    public void setRoleMenus(long roleId, List<Long> newMenus) {
        List<Long> oldMenus = roleMenuMapper.getRoleMenus(roleId);
        Set<Long> oldSet = new HashSet<>(oldMenus);
        Set<Long> newSet = new HashSet<>(newMenus);
        for (Long oldMenuId : oldMenus) {
            if (!newSet.contains(oldMenuId)) {
                roleMenuMapper.delete(roleId, oldMenuId);
            }
        }
        for (Long newMenuId : newMenus) {
            if (!oldSet.contains(newMenuId)) {
                roleMenuMapper.insert(new RoleMenu(newMenuId, roleId));
            }
        }
        
    }
    
    /**
     * 给用户添加角色
     *
     * @param userId 用户id
     * @param roleId 角色
     */
    @Override
    public void addRoleToUser(long userId, Long roleId) {
        Optional<Boolean> existsRelation = userRoleMapper.existsRelation(roleId, userId);
        if (existsRelation.orElse(false)) {
            return;
        }
        userRoleMapper.insert(new UserRole(userId, roleId));
    }
    
    /**
     * 给某个用户设置的角色，设置后仅有给定的角色
     *
     * @param userId   用户id
     * @param newRoles 角色列表
     * @return 完整角色列表
     */
    @Override
    public List<RoleDTO> setUserRoles(long userId, List<Long> newRoles) {
        Optional<Boolean> exists = userMapper.exists(userId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("找不到用户!");
        }
        List<Long> oldRoles = userRoleMapper.getUserRole(userId);
        
        Set<Long> oldSet = new HashSet<>(oldRoles);
        Set<Long> newSet = new HashSet<>(newRoles);
        for (Long oldRoleId : oldRoles) {
            if (!newSet.contains(oldRoleId)) {
                userRoleMapper.deleteRelation(oldRoleId, userId);
            }
        }
        for (Long newRoleId : newRoles) {
            if (!oldSet.contains(newRoleId)) {
                userRoleMapper.insert(new UserRole(userId, newRoleId));
            }
        }
        return this.getUserRole(userId);
    }
    
    private RoleDTO role2dto(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setCode(role.getCode());
        dto.setName(role.getName());
        dto.setOrderValue(role.getOrderValue());
        return dto;
    }
    
    private Role dto2role(RoleDTO dto) {
        Role role = new Role();
        role.setCode(dto.getCode());
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setOrderValue(dto.getOrderValue());
        
        return role;
    }
}
