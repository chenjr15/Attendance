package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.InputSysParameterDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sys-parameters")
@CrossOrigin(originPatterns = "*")
@Tag(name = "系统参数", description = "系统参数CRUD")
public class SysParameterController {
    @GetMapping("")
    @Operation(description = "列出所有参数")
    public RestResponse<List<InputSysParameterDTO>> listParameters(
            @RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize) {
//        ArrayList<List<String>> params = new ArrayList<>();
//        params.add(Arrays.asList("签到分值", ""));
        return RestResponse.okWithData(new ArrayList<>());
    }

    @PostMapping("")
    @Operation(description = "添加系统参数")
    public RestResponse<?> addSysParameter(@RequestBody InputSysParameterDTO parameterDTO) {
        return RestResponse.notImplemented();
    }


    @GetMapping("/{paramId}")
    @Operation(description = "显示系统参数信息")
    public RestResponse<InputSysParameterDTO> getSysParameter(@PathVariable Long paramId) {
        return RestResponse.okWithData(new InputSysParameterDTO());
    }

    @PutMapping("/{paramId}")
    @Operation(description = "修改系统参数")
    public RestResponse<?> modifySysParameter(@RequestBody InputSysParameterDTO parameterDTO, @PathVariable Long paramId) {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{paramId}")
    @Operation(description = "删除系统参数")
    public RestResponse<?> delSysParameter(@PathVariable Long paramId) {
        return RestResponse.notImplemented();
    }

}
