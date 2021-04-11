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

    private String name;

    private String description;

    private Integer state;

    private String schedule;

    @Schema(description = "学期")
    private String semester;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String teachers;

    private String location;

    public Course toCourse() {
        return dtoToCourse(this);
    }

    public static Course dtoToCourse(CourseDTO dto) {
        return new Course("",
                dto.name, dto.description, dto.state, dto.schedule, dto.semester,
                dto.startTime, dto.endTime, dto.teachers, dto.location, 0L);
    }
}
