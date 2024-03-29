package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "班课id", required = true)
    private Long courseId;
    
    
    @Schema(description = "经度", required = true)
    private Double longitude;
    
    
    @Schema(description = "纬度", required = true)
    private Double latitude;
    
    @NotNull
    @Schema(description = "签到类型")
    private long type;
    @Schema(description = "签到参数，依type而定，如手势")
    private String param;
    @Schema(description = "签到描述")
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "截止时间")
    private LocalDateTime deadline;
    
    @Schema(description = "签到是否结束")
    public boolean isFinished() {
        return deadline != null && deadline.isBefore(LocalDateTime.now());
    }
    
    @Schema(description = "发起人名称")
    String operatorName;
    @Schema(description = "发起人id")
    @JsonSerialize(using = ToStringSerializer.class)
    Long operatorId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    LocalDateTime startTime;
    
}
