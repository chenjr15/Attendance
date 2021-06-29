package dev.chenjr.attendance.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.chenjr.attendance.dao.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface UserRoleMapper extends MyBaseMapper<UserRole> {
    /**
     * @param id 指定的主键
     * @return !不存在返回 null, 存在返回true,
     */
    @Override
    @Select("SELECT 1 FROM user_role WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(long id);
    
    @Select("SELECT 1 FROM user_role WHERE role_id=#{roleId} AND user_id=#{userId} limit 1 ")
    Optional<Boolean> existsRelation(long roleId, long userId);
    
    @Delete("DELETE  FROM user_role WHERE role_id=#{roleId} AND user_id=#{userId} limit 1 ")
    void deleteRelation(long roleId, long userId);
    
    @Select("SELECT role_id FROM user_role WHERE user_id=#{userId}")
    List<Long> getUserRole(long userId);
    
    @Delete("DELETE  FROM user_role WHERE user_id=#{userId}")
    void removeAllRole(long userId);
    
    @Select("SELECT user_id FROM user_role WHERE role_id=#{roleId} ")
    <E extends IPage<Long>> E getRoleUsers(long roleId, E page);
    
    @Delete("DELETE  FROM user_role WHERE role_id=#{roleId}")
    void deleteByRoleId(long roleId);
}
