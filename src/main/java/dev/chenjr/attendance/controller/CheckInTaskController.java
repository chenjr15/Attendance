package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.CheckInTaskDTO;
import dev.chenjr.attendance.service.dto.CheckInTaskLogDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checkin-tasks")
@Tag(name = "签到", description = "发起签到和签到")
public class CheckInTaskController {

    @GetMapping("")
    @Operation(description = "列出所有签到任务")
    public List<CheckInTaskDTO> listCheckInTasks(
            @RequestParam(defaultValue = "1") long curPage, @RequestParam(defaultValue = "10") long pageSize) {
        return new ArrayList<>();
    }

    @PostMapping("")
    @Operation(description = "创建签到任务")
    public RestResponse<?> addCheckInTask(@RequestBody @Validated CheckInTaskDTO checkInTaskDTO) {
        return RestResponse.notImplemented();
    }

    @GetMapping("/courses/{courseId}")
    @Operation(description = "显示某个课程的签到任务信息")
    public RestResponse<?> getCheckInTaskOfCourse(@PathVariable Long courseId) {
        return RestResponse.notImplemented();
    }


    @GetMapping("/{taskId}")
    @Operation(description = "列出班课签到信息")
    public RestResponse<?> getCheckInTask(@PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }

    @PutMapping("/{taskId}")
    @Operation(description = "修改签到任务")
    public RestResponse<?> modifyCheckInTask(
            @RequestBody @Validated CheckInTaskDTO checkInTaskDTO,
            @PathVariable Long taskId
    ) {
        return RestResponse.notImplemented();
    }

    @GetMapping("/{taskId}/logs")
    @Operation(description = "获取某个签到任务的签到记录")
    public RestResponse<?> listCheckInLogs(
            @RequestBody @Validated CheckInTaskLogDTO checkInTaskLogDTO,
            @PathVariable Long taskId
    ) {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{taskId}")
    @Operation(description = "删除签到任务")
    public RestResponse<?> delCheckInTask(@PathVariable Long taskId) {
        return RestResponse.notImplemented();
    }


    @PatchMapping("/{taskId}/logs")
    @Operation(description = "修改签到任务")
    public RestResponse<?> modifyCheckInTaskLog(
            @RequestBody @Validated CheckInTaskDTO checkInTaskDTO,
            @PathVariable Long taskId
    ) {
        return RestResponse.notImplemented();
    }

    @PostMapping("/{taskId}/logs")
    @Operation(description = "签到")
    public RestResponse<?> checkIn(
            @RequestBody @Validated CheckInTaskLogDTO checkInTaskLogDTO,
            @PathVariable Long taskId
    ) {
        return RestResponse.notImplemented();
    }
}
