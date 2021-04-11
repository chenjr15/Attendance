package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户选课表，记录用户和所选课程，包含，如果类型是学生，则说明该学生选择了该课程，如果类型是老师，则说明该用户是该课程的老师
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户选课表，记录用户和所选课程，包含，如果类型是学生，则说明该学生选择了该课程，如果类型是老师，则说明该用户是该课程的老师")
public class UserCourse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(name = "该用户在课程中是什么角色，是学生或老师，连接到字典表")
    private Integer characterType;

    private Long userId;

    private Long courseId;

    @Schema(name = "经验值")
    private Long experience;


}
