package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface RoleMapper extends MyBaseMapper<Role> {
    /**
     * 必须重写才能用！
     * 判断指定id的记录是否存在
     * 注意这里不能用boolean, 必须要用包装类, 找不到的时候会返回null, 因此需要判断是否为空
     * 优化：select 1 意味着存在就会返回1, limit 1 表示只要查一行即可(虽然也最多只有一行)
     *
     * @param id 指定的主键
     * @return !不存在返回 null, 存在返回true,
     */
    @Override
    @Select("SELECT 1 FROM role WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(long id);
}
