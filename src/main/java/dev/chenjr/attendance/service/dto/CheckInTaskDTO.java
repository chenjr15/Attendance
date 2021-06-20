package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CheckInTaskDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @NotNull
    @Schema(description = "班课号", required = true)
    private Long courseId;
    @Schema(description = "签到地点")
    private String location;
    @NotNull
    @Schema(description = "签到类型")
    private Long type;
    @Schema(description = "签到参数，依type而定，如手势")
    private String param;
    @Schema(description = "签到描述")
    private String description;
    @NotNull
    @Schema(description = "截止时间")
    private LocalDateTime deadline;
    @Schema(description = "签到是否结束")
    private Boolean finished;
}
