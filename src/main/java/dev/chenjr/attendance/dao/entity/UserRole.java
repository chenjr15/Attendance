package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色关联")
public class UserRole extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private Long roleId;
    
    public UserRole(long uid, long rid) {
        super();
        this.userId = uid;
        this.roleId = rid;
    }
    
    
}
