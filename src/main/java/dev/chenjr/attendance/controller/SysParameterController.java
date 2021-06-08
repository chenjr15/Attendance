package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.ISysParamService;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.SysParameterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

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

    @PutMapping("/{paramCode}")
    @Operation(description = "修改系统参数")
    public RestResponse<?> modifySysParameter(@RequestBody SysParameterDTO parameterDTO, @PathVariable @NotBlank String paramCode) {
        if (paramCode.equals(parameterDTO.getCode())) {
            sysParamService.updateSystemParams(parameterDTO);
            return RestResponse.ok();
        }
        throw new SuperException("paramCode mismatch!");
    }

    @PostMapping("/")
    @Operation(description = "添加系统参数")
    public RestResponse<?> createSysParameter(@RequestBody SysParameterDTO parameterDTO) {
        sysParamService.createSystemParams(parameterDTO);
        return RestResponse.ok();
    }

    @DeleteMapping("/{paramCode}")
    @Operation(description = "删除指定系统参数")
    public RestResponse<?> deleteSysParameter(@PathVariable @NotBlank String paramCode) {
        sysParamService.deleteByCode(paramCode);
        return RestResponse.okWithMsg("Deleted");
    }

    @DeleteMapping("/")
    @Operation(description = "!清空!系统参数")
    public RestResponse<?> deleteAllSysParameter(@RequestParam(defaultValue = "false") Boolean confirm) {
        if (confirm) {
            this.sysParamService.deleteAll();
            return RestResponse.okWithMsg("Deleted!");
        }
        throw new HttpStatusException(HttpStatus.NOT_MODIFIED);
    }

}
