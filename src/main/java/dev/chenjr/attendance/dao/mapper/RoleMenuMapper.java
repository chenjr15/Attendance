package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
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
public interface RoleMenuMapper extends MyBaseMapper<RoleMenu> {
    @Select("SELECT menu_id FROM role_menu WHERE role_id=#{roleId} ")
    List<Long> getRoleMenus(long roleId);
    
    @Select("SELECT 1 FROM role_menu WHERE role_id=#{roleId} AND menu_id=#{menuId}")
    Boolean exists(long roleId, long menuId);
    
    @Delete("DELETE  FROM role_menu WHERE role_id=#{roleId} AND menu_id=#{menuId}")
    void delete(long roleId, long menuId);
}
