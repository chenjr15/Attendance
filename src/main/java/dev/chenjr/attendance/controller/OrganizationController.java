package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IOrganizationService;
import dev.chenjr.attendance.service.dto.OrganizationDTO;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/organizations")
@Tag(name = "组织结构", description = "学校和地区等多级嵌套的结构")
public class OrganizationController {
    @Autowired
    IOrganizationService organizationService;

    @GetMapping("/schools")
    @Operation(description = "返回学校")
    public RestResponse<PageWrapper<OrganizationDTO>> listSchool(
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<OrganizationDTO> organizationPW = organizationService.listPage("school", pageSort);
        return RestResponse.okWithData(organizationPW);
    }


    @GetMapping("/locations")
    @Operation(description = "返回省级行政区划")
    public RestResponse<PageWrapper<OrganizationDTO>> listLocation(
            @ParameterObject PageSort pageSort
    ) {
        log.info("pageSort：{}", pageSort);

        PageWrapper<OrganizationDTO> organizationPW = organizationService.listPage("location", pageSort);

        return RestResponse.okWithData(organizationPW);
    }


    @GetMapping("/{orgId}")
    @Operation(description = "显示某个组织结构信息,包括其儿子节点(仅一级儿子)")
    public RestResponse<OrganizationDTO> getOrg(@PathVariable long orgId) {
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
    @Operation(description = "删除指定节点")
    public RestResponse<?> delOrg(
            @PathVariable long orgId) {

        organizationService.delete(orgId);

        return RestResponse.okWithMsg("Deleted");
    }


}
