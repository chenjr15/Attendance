package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Course对象")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String code;

    private String name;

    private String description;

    private Integer state;

    private String schedule;

    @Schema(description = "学期")
    private String semester;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    // private Long checkCount;
    @Schema(description = "学校院系专业的ID")
    private Long schoolMajor;

}
