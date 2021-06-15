package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "课程id")
    private Long id;
    @Schema(description = "课程代码")
    private String code;

    @Schema(required = true)
    @Length(min = 4, max = 256)
    private String name;
    @Length(max = 1024)
    private String description;

    @Schema(defaultValue = "0")
    private Integer state;
    private String stateName;

    @Length(max = 256)
    @Schema(description = "上课时间信息，文本描述")
    private String schedule;

    @Length(max = 256)
    @Schema(description = "学期")
    private String semester;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Length(max = 256)
    private String location;

    @Schema(description = "签到次数统计")
    private long checkCount;

    @Schema(description = "学校院系专业ID", example = "102210")
    @JsonSerialize(using = ToStringSerializer.class)
    private long schoolMajorID;
    @Schema(description = "学校院系专业名", example = "xx大学-计算机学院-软件工程专业-软工1班")
    private String schoolMajorName;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "教师id")
    private Long teacherId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "教师名字")
    private String teacherName;
}
