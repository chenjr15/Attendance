package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupTaskDTO {

    @Schema(description = "班课号")
    private Long courseId;
    @Schema(description = "签到地点")
    private String location;
    @Schema(description = "签到类型")
    private Long type;
    @Schema(description = "附加信息，依type而定")
    private Long ext;
    @Schema(description = "签到描述")
    private String description;
    @Schema(description = "截止时间")
    private LocalDateTime deadline;
}
