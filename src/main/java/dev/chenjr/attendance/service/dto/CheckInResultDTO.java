package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CheckInResultDTO extends CourseStudentDTO {

    @Schema(description = "签到状态")
    private String statusName;
    @Schema(description = "签到状态")
    private int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "经度", required = true)
    private Double longitude;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "纬度", required = true)
    private Double latitude;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "距离")
    private Double distance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "签到时间")
    LocalDateTime checkTime;

}
