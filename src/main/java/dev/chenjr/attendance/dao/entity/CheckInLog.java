package dev.chenjr.attendance.dao.entity;

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
public class CheckInLog extends BaseEntity {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_LEAVE = 1;
    public static final int STATUS_LATE = 2;
    public static final int STATUS_ABSENCE = 3;

    private static final long serialVersionUID = 1L;

    /**
     * 签到状态，
     * - 0 正常签到
     * - 1 请假
     * - 2 迟到
     * - 3 未签到
     */
    private int status;

    private long taskId;

    private long uid;

    private long courseId;

    private Double longitude;
    private Double latitude;

    /**
     * 签到距离
     */
    private double distance;

    /**
     * 获得的经验值
     */
    private int experience;
}
