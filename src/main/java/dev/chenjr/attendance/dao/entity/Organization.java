package dev.chenjr.attendance.dao.entity;

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
@Schema(description = "组织机构表，学校-> 院系->专业->班级")
public class Organization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    
    private Long parentId;

    private String name;
    private String parents;
    private String comment;
    private String code;
    @Schema(description = "类型，院校为0", defaultValue = "0")
    private Integer orgType;
    @Schema(description = "所属省份，，0为其他", defaultValue = "0")
    private Long province;
}
