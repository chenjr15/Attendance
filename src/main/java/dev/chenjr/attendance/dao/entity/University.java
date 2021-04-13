package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学校信息表，学校-> 院系->专业->班级
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "学校信息表，学校-> 院系->专业->班级")
@TableName("University")
public class University extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long parentId;

    private String name;

    private String comment;

    private String code;


}
