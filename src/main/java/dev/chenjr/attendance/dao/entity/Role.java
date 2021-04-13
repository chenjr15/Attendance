package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色表")
@TableName("Role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;


}
