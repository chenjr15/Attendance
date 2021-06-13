package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CheckInTaskLogDTO {

    @NotNull
    @Schema(description = "学生id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @NotNull
    @Schema(description = "签到任务id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long taskId;
    @NotBlank
    @Schema(description = "签到地点(gps)")
    private String location;

}
