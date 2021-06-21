package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.ICheckInService;
import dev.chenjr.attendance.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkin-tasks")
@Tag(name = "签到", description = "发起签到和签到")
public class CheckInTaskController {
    @Autowired
    ICheckInService checkInService;

    @GetMapping("")
    @Operation(description = "列出所有签到任务")
    public RestResponse<PageWrapper<CheckInTaskDTO>> listCheckInTasks(
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<CheckInTaskDTO> pageWrapper = checkInService.listAllTasks(pageSort);
        return RestResponse.okWithData(pageWrapper);
    }

    @PostMapping("")
    @Operation(description = "创建签到任务")
    public RestResponse<?> createCheckInTask(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody @Validated CheckInTaskDTO checkInTaskDTO
    ) {
        checkInTaskDTO.setOperatorId(user.getId());
        checkInTaskDTO = checkInService.createCheckInTask(checkInTaskDTO);
        return RestResponse.okWithData(checkInTaskDTO);
    }

    @GetMapping("/courses/{courseId}")
    @Operation(description = "显示某个课程的签到任务信息，无签到日志")
    public RestResponse<PageWrapper<CheckInTaskDTO>> getCheckInTaskOfCourse(
            @PathVariable long courseId,
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<CheckInTaskDTO> pageWrapper = checkInService.listCourseTasks(courseId, pageSort);
        return RestResponse.okWithData(pageWrapper);
    }


    @GetMapping("/{taskId}")
    @Operation(description = "列出某次签到的信息,具体签到信息比较长，请单独获取")
    public RestResponse<CheckInTaskDTO> getCheckInTask(@PathVariable long taskId) {
        CheckInTaskDTO task = checkInService.getTask(taskId);
        return RestResponse.okWithData(task);
    }

    @PatchMapping("/{taskId}")
    @Operation(description = "修改签到任务")
    public RestResponse<CheckInTaskDTO> modifyCheckInTask(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody @Validated CheckInTaskDTO checkInTaskDTO,
            @PathVariable long taskId
    ) {
        checkInTaskDTO.setId(taskId);
        CheckInTaskDTO task = checkInService.modifyTask(user, checkInTaskDTO);
        return RestResponse.okWithData(task);
    }

    @GetMapping("/{taskId}/logs")
    @Operation(description = "获取某个签到任务的签到记录")
    public RestResponse<PageWrapper<CheckInLogDTO>> listCheckInLogs(
            @PathVariable Long taskId,
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<CheckInLogDTO> logs;
        logs = checkInService.listCheckInLogs(taskId, pageSort);
        return RestResponse.okWithData(logs);
    }

    @DeleteMapping("/{taskId}")
    @Operation(description = "删除签到任务")
    public RestResponse<?> delCheckInTask(@PathVariable Long taskId) {
        checkInService.deleteTask(taskId);
        return RestResponse.ok();
    }

    @PostMapping("/{taskId}/ended")
    @Operation(description = "结束签到")
    public RestResponse<?> endCheckInTask(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @PathVariable long taskId
    ) {

        checkInService.endCheckInTask(user, taskId);
        return RestResponse.ok();
    }

    @PatchMapping("/{taskId}/logs/{logId}")
    @Operation(description = "修改签到记录")
    public RestResponse<?> modifyCheckInTaskLog(
            @RequestBody @Validated CheckInLogDTO logDTO,
            @PathVariable long taskId,
            @PathVariable long logId
    ) {
        logDTO.setId(logId);
        logDTO = checkInService.modifyLog(logDTO);
        return RestResponse.okWithData(logDTO);
    }

    @PostMapping("/{taskId}/logs")
    @Operation(description = "签到, 需要传入经纬度")
    public RestResponse<?> checkIn(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody @Validated CheckInLogDTO logDTO,
            @PathVariable Long taskId
    ) {
        logDTO = checkInService.checkIn(user, taskId, logDTO);
        return RestResponse.okWithData(logDTO);
    }
}
