package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 签到记录表，记录用户在每门课的签到时间签到地点，对应的签到任务id
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "签到记录，记录用户在每门课的签到时间签到地点，对应的签到任务id")
@TableName("SignupLog")
public class SignupLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    private Long userId;

    private Long courseId;

    private String location;

    private Long type;

    private String ext;


}
