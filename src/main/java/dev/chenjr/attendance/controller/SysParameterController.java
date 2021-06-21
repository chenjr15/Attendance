package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.ISysParamService;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.SysParameterDTO;
import dev.chenjr.attendance.service.dto.validation.KeyWord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys-parameters")
@Tag(name = "系统参数", description = "系统参数一般只能更新")
public class SysParameterController {
    @Autowired
    ISysParamService sysParamService;

    @GetMapping("")
    @Operation(description = "列出所有参数")
    public RestResponse<PageWrapper<SysParameterDTO>> listParameters(
            @RequestParam(defaultValue = "1") long curPage, @RequestParam(defaultValue = "10") long pageSize) {
        PageWrapper<SysParameterDTO> allSystemParams = sysParamService.listSystemParams(curPage, pageSize);
        return RestResponse.okWithData(allSystemParams);
    }

    @PostMapping("/init")
    @Operation(description = "初始化系统参数(仅供调试,添加测试数据)")
    public RestResponse<?> initSysParameter() {
        // 仅在系统参数为空的时候有效
        sysParamService.initSysParams();
        return RestResponse.ok();
    }


    @GetMapping("/{paramCode}")
    @Operation(description = "显示系统参数信息")
    public RestResponse<SysParameterDTO> getSysParameter(@PathVariable String paramCode) {
        SysParameterDTO systemParam = sysParamService.getSystemParam(paramCode);
        return RestResponse.okWithData(systemParam);
    }

    @PatchMapping("/{paramCode}")
    @Operation(description = "修改系统参数, body中的 paramCode 可以不填，会被覆盖url中的paramCode")
    public RestResponse<?> modifySysParameter(
            @RequestBody @Validated SysParameterDTO parameterDTO,
            @PathVariable @KeyWord String paramCode) {
        parameterDTO.setCode(paramCode);
        sysParamService.updateSystemParams(parameterDTO);
        return RestResponse.ok();
    }

    @PostMapping("")
    @Operation(description = "添加系统参数")
    public RestResponse<?> createSysParameter(
            @RequestBody @Validated SysParameterDTO parameterDTO) {
        sysParamService.createSystemParams(parameterDTO);
        return RestResponse.ok();
    }

    @DeleteMapping("/{paramCode}")
    @Operation(description = "删除指定系统参数")
    public RestResponse<?> deleteSysParameter(
            @PathVariable @KeyWord String paramCode) {

        sysParamService.deleteByCode(paramCode);

        return RestResponse.okWithMsg("Deleted");
    }

    @DeleteMapping("")
    @Operation(description = "!清空!系统参数")
    public RestResponse<?> deleteAllSysParameter(@RequestParam(defaultValue = "false") Boolean confirm) {
        if (confirm) {
            this.sysParamService.deleteAll();
            return RestResponse.okWithMsg("Deleted!");
        }
        throw new HttpStatusException(HttpStatus.NOT_MODIFIED);
    }

}
