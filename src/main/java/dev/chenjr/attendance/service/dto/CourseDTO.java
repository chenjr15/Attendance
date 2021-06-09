package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.dao.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    @Schema(required = true)
    private String name;

    private String description;

    @Schema(defaultValue = "0")
    private Integer state;

    @Schema(description = "上课时间信息，文本描述")
    private String schedule;

    @Schema(description = "学期")
    private String semester;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    private String location;

    private Long checkCount;

    @Schema(description = "学校院系专业ID", example = "102210")
    private Long schoolMajorID;
    @Schema(description = "学校院系专业名", example = "xx大学-计算机学院-软件工程专业-软工1班")
    private String schoolMajorName;

    public Course toCourse() {
        return dtoToCourse(this);
    }

    public static Course dtoToCourse(CourseDTO dto) {
        return new Course("",
                dto.name, dto.description, dto.state, dto.schedule, dto.semester,
                dto.startTime, dto.endTime, dto.location, 0L);
    }
}
