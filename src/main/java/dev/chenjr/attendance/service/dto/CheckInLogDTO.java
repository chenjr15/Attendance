package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CheckInLogDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "签到状态")
    private int status;

    @NotNull
    @Schema(description = "学生id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;
    
    @Schema(description = "学工号")
    @JsonSerialize(using = ToStringSerializer.class)
    private String stuId;

    @Schema(description = "学生姓名")
    private String stuName;

    @NotNull
    @Schema(description = "签到任务id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long taskId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "班课id")
    private Long courseId;

    @Schema(description = "经度", required = true)
    private Double longitude;

    @Schema(description = "纬度", required = true)
    private Double latitude;

    @Schema(description = "距离")
    private Double distance;

    @Schema(description = "签到参数")
    private String param;

    @Schema(description = "获得的经验值")
    private Integer experience;
}
