package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Schema(description = "存储每次签到任务")
public class CheckInTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long courseId;

    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 截至时间
     */
    private LocalDateTime deadline;
    /**
     * 签到是否结束
     */
    private Boolean finished;

    /**
     * 签到类型
     * - 0 一键签到
     * - 1 限时签到
     * - 2 手势签到
     */
    private Long type;

    private String description;
    /**
     * 签到参数，如手势签到的手势
     */
    private String param;


}
