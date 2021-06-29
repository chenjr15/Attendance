package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IRoleService;
import dev.chenjr.attendance.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@Tag(name = "角色", description = "权限、角色")
public class RoleController {
    
    @Autowired
    IRoleService roleService;
    
    @Operation(description = "获取所有角色")
    @SecurityRequirements
    @GetMapping("")
    public RestResponse<List<RoleDTO>> listAllRole() {
        List<RoleDTO> roleDTOS = roleService.listRole();
        return RestResponse.okWithData(roleDTOS);
    }
    
    @Operation(description = "获取角色信息")
    @GetMapping("/{roleId}")
    public RestResponse<RoleDTO> getRole(
            @PathVariable long roleId
    ) {
        
        RoleDTO role = roleService.getRole(roleId);
        return RestResponse.okWithData(role);
    }
    
    @GetMapping("/{roleId}/users")
    @Operation(description = "获取某个角色下的用户")
    public RestResponse<PageWrapper<UserDTO>> getRoleUsers(
            @PathVariable long roleId,
            @ParameterObject PageSort pageSort
    ) {
        
        PageWrapper<UserDTO> roles = roleService.getRoleUsers(roleId, pageSort);
        return RestResponse.okWithData(roles);
    }
    
    @GetMapping("/{roleId}/menus")
    @Operation(description = "获取某个角色可访问的菜单")
    public RestResponse<PageWrapper<MenuDTO>> getRoleMenus(
            @PathVariable long roleId
    ) {
        
        PageWrapper<MenuDTO> roles = roleService.getRoleMenus(roleId);
        return RestResponse.okWithData(roles);
    }
    
    @PostMapping("/{roleId}/menus/{menuId}")
    @Operation(description = "增加某个角色可以访问的菜单项")
    public RestResponse<?> addRoleMenus(
            @PathVariable long roleId,
            @PathVariable long menuId
    
    ) {
        
        roleService.addMenuToRole(roleId, menuId);
        return RestResponse.ok();
    }
    
    @PostMapping("/{roleId}/menus")
    @Operation(description = "批量增加某个角色可以访问的菜单项")
    public RestResponse<?> addRoleMenus(
            @PathVariable long roleId,
            @RequestBody List<Long> menuList
    
    ) {
        
        roleService.addMenuToRole(roleId, menuList);
        return RestResponse.ok();
    }
    
    @PutMapping("/{roleId}/menus")
    @Operation(description = "设置某个角色可以访问的菜单项，输入什么菜单该角色就只能访问这些菜单，不多不少")
    public RestResponse<?> getRoleMenus(
            @PathVariable long roleId,
            @RequestBody List<Long> menuList
    
    ) {
        
        roleService.setRoleMenus(roleId, menuList);
        return RestResponse.ok();
    }
    
    @DeleteMapping("/{roleId}/menus")
    @Operation(description = "批量删除某个角色可以访问的菜单项")
    public RestResponse<?> deleteRoleMenus(
            @PathVariable long roleId,
            @RequestBody List<Long> menuList
    
    ) {
        roleService.removeMenuToRole(roleId, menuList);
        return RestResponse.ok();
    }
    
    @DeleteMapping("/{roleId}/menus/{menuId}")
    @Operation(description = "删除某个角色可以访问的菜单项")
    public RestResponse<?> deleteRoleMenus(
            @PathVariable long roleId,
            @PathVariable long menuId
    
    ) {
        
        roleService.removeMenuToRole(roleId, menuId);
        return RestResponse.ok();
    }
    
    @Operation(description = "创建角色")
    @PostMapping("")
    public RestResponse<RoleDTO> createRole(
            @RequestBody RoleDTO toCreate
    ) {
        RoleDTO created = roleService.createRole(toCreate);
        return RestResponse.okWithData(created);
    }
    
    @Operation(description = "修改角色")
    @PatchMapping("/{roleId}")
    public RestResponse<RoleDTO> modifyRole(
            @PathVariable long roleId,
            @RequestBody RoleDTO toModify
    ) {
        toModify.setId(roleId);
        RoleDTO modified = roleService.modifyRole(toModify);
        return RestResponse.okWithData(modified);
    }
    
    @Operation(description = "删除")
    @DeleteMapping("/{roleId}")
    public RestResponse<?> deleteRole(
            @PathVariable long roleId
    ) {
        roleService.deleteRole(roleId);
        return RestResponse.ok();
    }
    
    @Operation(description = "获取某个用户的角色")
    @GetMapping("/users/{userId}")
    public RestResponse<List<RoleDTO>> getUserRole(
            @PathVariable long userId
    ) {
        List<RoleDTO> role = roleService.getUserRole(userId);
        return RestResponse.okWithData(role);
    }
    
    
    @Operation(description = "给用户设置角色")
    @PostMapping("/{roleId}/users/{userId}")
    public RestResponse<List<RoleDTO>> getUserRole(
            @PathVariable long userId,
            @PathVariable long roleId
    
    ) {
        List<RoleDTO> roleLIst = roleService.addRoleByCodeToUser(userId, roleId);
        return RestResponse.okWithData(roleLIst);
    }
    
    @Operation(description = "给用户删除角色")
    @DeleteMapping("/{roleId}/users/{userId}")
    public RestResponse<List<RoleDTO>> removeUserRole(
            @PathVariable long userId,
            @PathVariable long roleId
    
    ) {
        List<RoleDTO> roleLIst = roleService.removeRoleOfUser(userId, roleId);
        return RestResponse.okWithData(roleLIst);
    }
    
    @Operation(description = "给某个用户设置的角色，设置后仅有给定的角色")
    @PutMapping("/users/{userId}")
    public RestResponse<List<RoleDTO>> setUserRoles(
            @PathVariable long userId,
            @RequestBody List<Long> roleIds
    
    ) {
        List<RoleDTO> roleLIst = roleService.setUserRoles(userId, roleIds);
        return RestResponse.okWithData(roleLIst);
    }
}
