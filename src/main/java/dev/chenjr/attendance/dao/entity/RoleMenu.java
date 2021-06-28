package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色菜单权限关联")
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    private Long menuId;
    
    private Long roleId;
    
    
}
