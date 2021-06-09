package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class InputCheckInTaskLogDTO {

    @NonNull
    @Schema(description = "学生id")
    private Long userId;
    @NonNull
    @Schema(description = "签到任务id")
    private Long taskId;
    @NotBlank
    @Schema(description = "签到地点(gps)")
    private String location;

}
