package dev.chenjr.attendance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;


/**
 * 添加
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Course extends BaseEntity {
    public static final int STATE_PREPARING = 1;
    public static final int STATE_OPENING = 2;
    public static final int STATE_CLOSED = 3;
    // 课程号，唯一，随机
    private String code;
    private String name;
    @Schema(description = "课程的时间安排", example = "周二1-4节")
    private String schedule;
    @Schema(description = "课程的开课学期", example = "2021-spring")
    private String semester;
    @Schema(description = "创建者的uid", example = "100324")
    private int creator;
    @Schema(description = "课程状态：未开课，开课中，已结课", example = "1")
    private int state;
    @Schema(description = "开课日期")
    private Timestamp openDate;


}
