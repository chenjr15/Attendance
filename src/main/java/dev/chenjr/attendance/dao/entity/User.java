package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户表，所有系统内的用户都在这个表里
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户表，所有系统内的用户都在这个表里")
@TableName("User")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String realName;

    private Integer gender;

    private String email;

    private String phone;

    @Schema(description = "学工号，可能会有字母")
    private String academicId;
    
    @Schema(description = "学校院系专业的ID")
    private Long schoolMajor;


}