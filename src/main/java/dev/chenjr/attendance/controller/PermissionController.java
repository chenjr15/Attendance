package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IPermissionService;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.PermissionDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perms")
public class PermissionController {
    @Autowired
    IPermissionService permService;
    
    @GetMapping("")
    public RestResponse<PageWrapper<PermissionDTO>> listPerms(
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<PermissionDTO> pageWrapper = permService.listPerms(pageSort);
        return RestResponse.okWithData(pageWrapper);
    }
    
    @PostMapping("")
    public RestResponse<PermissionDTO> createPerm(
            @RequestBody @Validated PermissionDTO dto
    ) {
        PermissionDTO created = permService.create(dto);
        return RestResponse.okWithData(created);
    }
    
    @DeleteMapping("/{permId}")
    public RestResponse<?> deletePerm(
            @PathVariable long permId
    ) {
        permService.delete(permId);
        return RestResponse.ok();
    }
    
    @PostMapping("/{permId}")
    public RestResponse<PermissionDTO> modifyPerm(
            @PathVariable long permId,
            @RequestBody @Validated PermissionDTO dto
    ) {
        dto.setId(permId);
        PermissionDTO modified = permService.modify(dto);
        return RestResponse.okWithData(modified);
    }
    
    @GetMapping("/{permId}")
    public RestResponse<PermissionDTO> getPerm(
            @PathVariable long permId
    ) {
        PermissionDTO got = permService.getPerm(permId);
        return RestResponse.okWithData(got);
    }
}
