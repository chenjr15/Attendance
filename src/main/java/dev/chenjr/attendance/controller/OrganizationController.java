package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IOrganizationService;
import dev.chenjr.attendance.service.dto.OrganizationDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
@Tag(name = "组织结构", description = "xx大学-xx学院-xx专业-xx班级")
public class OrganizationController {
    @Autowired
    IOrganizationService organizationService;

    @GetMapping("/{orgType}")
    @Operation(description = "列出指定类型组织结构的顶层")
    public RestResponse<PageWrapper<OrganizationDTO>> listOrgPage(
            @PathVariable String orgType,
            @RequestParam(defaultValue = "1") long curPage,
            @RequestParam(defaultValue = "10") long pageSize
    ) {
        PageWrapper<OrganizationDTO> organizationPW = organizationService.listPage(orgType, curPage, pageSize);
        return RestResponse.okWithData(organizationPW);
    }


    @GetMapping("/{orgId}")
    @Operation(description = "显示某个组织结构信息,包括其儿子节点(仅一级儿子)")
    public RestResponse<OrganizationDTO> getOrg(@PathVariable Long orgId) {
        OrganizationDTO org = organizationService.fetch(orgId);
        return RestResponse.okWithData(org);
    }

    @PutMapping("/{orgId}")
    @Operation(description = "修改某个节点信息, 返回修改后的信息" +
            "- body中的 id 可以不填，会被url中的id覆盖" +
            "- 不会修改儿子节点")
    public RestResponse<OrganizationDTO> modifyOrg(
            @RequestBody @Validated OrganizationDTO orgDTO,
            @PathVariable long orgId) {
        orgDTO.setId(orgId);
        OrganizationDTO modified = organizationService.modify(orgDTO);
        return RestResponse.okWithData(modified);
    }

    @PostMapping("/")
    @Operation(description = "添加某个指定类型的节点")
    public RestResponse<OrganizationDTO> createOrg(
            @RequestBody @Validated OrganizationDTO organizationDTO) {
        OrganizationDTO created = organizationService.create(organizationDTO);
        return RestResponse.okWithData(created);
    }

    @DeleteMapping("/{orgId}")
    @Operation(description = "删除指定系统参数")
    public RestResponse<?> delOrg(
            @PathVariable long orgId) {

        organizationService.delete(orgId);

        return RestResponse.okWithMsg("Deleted");
    }


}
