package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.InputSysParameterDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/sys-parameters")
@RestController
public class SysParameterController {
    @GetMapping("")
    @Operation(description = "列出所有参数")
    @ResponseBody
    public RestResponse<List<InputSysParameterDTO>> listParameters(
            @RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize) {
//        ArrayList<List<String>> params = new ArrayList<>();
//        params.add(Arrays.asList("签到分值", ""));
        return RestResponse.okWithData(new ArrayList<>());
    }

    @PostMapping("")
    @Operation(description = "添加系统参数")
    @ResponseBody
    public RestResponse<?> addSysParameter(@RequestBody InputSysParameterDTO parameterDTO) {
        return RestResponse.notImplemented();
    }


    @GetMapping("/{paramId}")
    @Operation(description = "显示系统参数信息")
    @ResponseBody
    public RestResponse<InputSysParameterDTO> getSysParameter(@PathVariable Long paramId) {
        return RestResponse.okWithData(new InputSysParameterDTO());
    }

    @PutMapping("/{paramId}")
    @Operation(description = "修改系统参数")
    @ResponseBody
    public RestResponse<?> modifySysParameter(@RequestBody InputSysParameterDTO parameterDTO, @PathVariable Long paramId) {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{paramId}")
    @Operation(description = "删除系统参数")
    @ResponseBody
    public RestResponse<?> delSysParameter(@PathVariable Long paramId) {
        return RestResponse.notImplemented();
    }

}