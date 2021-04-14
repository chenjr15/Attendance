package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Schema(description = "存储每次签到任务")
public class SignupTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long courseId;

    private String location;

    private Long type;

    private String description;


}
