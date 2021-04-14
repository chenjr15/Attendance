package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.InputSignupTaskDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.SignupTaskDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/signup-tasks")
@RestController
public class SignupTaskController {

    @GetMapping("")
    @Operation(description = "列出所有签到任务")
    @ResponseBody
    public List<SignupTaskDTO> listSignupTasks(
            @RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize) {
        return new ArrayList<>();
    }

    @PostMapping("")
    @Operation(description = "创建签到任务")
    @ResponseBody
    public RestResponse<?> addSignupTask(@RequestBody SignupTaskDTO parameterDTO) {
        return RestResponse.notImplemented();
    }

    @GetMapping("/{courseId}")
    @Operation(description = "显示某个课程的签到任务信息")
    @ResponseBody
    public RestResponse<?> getSignupTask(@PathVariable Long courseId, @PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }


    @GetMapping("/{taskId}")
    @Operation(description = "列出班课信息")
    @ResponseBody
    public RestResponse<?> getSignupTask(@PathVariable Long courseId) {
        return RestResponse.notImplemented();
    }

    @PutMapping("/{taskId}")
    @Operation(description = "修改签到任务")
    @ResponseBody
    public RestResponse<?> modifySignupTask(@RequestBody SignupTaskDTO parameterDTO, @PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }


    @DeleteMapping("/{taskId}")
    @Operation(description = "删除签到任务")
    @ResponseBody
    public RestResponse<?> delSignupTask(@PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }


    @PutMapping("/{taskId}/log")
    @Operation(description = "修改签到记录")
    @ResponseBody
    public RestResponse<?> modifySignupTaskLog(@RequestBody SignupTaskDTO parameterDTO, @PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }

    @PostMapping("/{taskId}/log/")
    @Operation(description = "签到")
    @ResponseBody
    public RestResponse<?> signup(@RequestBody InputSignupTaskDTO signupTaskDTO, @PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }
}
