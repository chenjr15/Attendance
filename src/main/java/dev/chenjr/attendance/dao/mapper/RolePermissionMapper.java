package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.RolePermission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface RolePermissionMapper extends MyBaseMapper<RolePermission> {
    @Select("SELECT user_id FROM role_permission WHERE role_id=#{roleId} ")
    List<Long> getRolePerms(long roleId);
}
