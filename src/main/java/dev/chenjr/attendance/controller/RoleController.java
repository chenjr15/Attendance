package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IRoleService;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.RoleDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "角色", description = "权限、角色")
public class RoleController {
    
    @Autowired
    IRoleService roleService;
    
    @GetMapping("")
    public RestResponse<List<RoleDTO>> listAllRole() {
        List<RoleDTO> roleDTOS = roleService.listRole();
        return RestResponse.okWithData(roleDTOS);
    }
    
    @GetMapping("/{roleId}")
    public RestResponse<RoleDTO> getRole(
            @PathVariable long roleId
    ) {
        
        RoleDTO role = roleService.getRole(roleId);
        return RestResponse.okWithData(role);
    }
    
    @PostMapping("")
    public RestResponse<RoleDTO> createRole(
            @RequestBody RoleDTO toCreate
    ) {
        RoleDTO created = roleService.createRole(toCreate);
        return RestResponse.okWithData(created);
    }
    
    
    @PatchMapping("/{roleId}")
    public RestResponse<RoleDTO> modifyRole(
            @PathVariable long roleId,
            @RequestBody RoleDTO toModify
    ) {
        toModify.setId(roleId);
        RoleDTO modified = roleService.modifyRole(toModify);
        return RestResponse.okWithData(modified);
    }
    
    @DeleteMapping("/{roleId}")
    public RestResponse<?> deleteRole(
            @PathVariable long roleId
    ) {
        roleService.deleteRole(roleId);
        return RestResponse.ok();
    }
    
    @GetMapping("/users/{userId}")
    public RestResponse<List<RoleDTO>> getUserRole(
            @PathVariable long userId
    ) {
        List<RoleDTO> role = roleService.getUserRole(userId);
        return RestResponse.okWithData(role);
    }
    
    
    @PostMapping("/users/{userId}/{roleId}")
    public RestResponse<List<RoleDTO>> getUserRole(
            @PathVariable long userId,
            @PathVariable long roleId
    
    ) {
        List<RoleDTO> roleLIst = roleService.addRoleToUser(userId, roleId);
        return RestResponse.okWithData(roleLIst);
    }
    
    @DeleteMapping("/users/{userId}/{roleId}")
    public RestResponse<List<RoleDTO>> removeUserRole(
            @PathVariable long userId,
            @PathVariable long roleId
    
    ) {
        List<RoleDTO> roleLIst = roleService.removeRoleToUser(userId, roleId);
        return RestResponse.okWithData(roleLIst);
    }
    
}
