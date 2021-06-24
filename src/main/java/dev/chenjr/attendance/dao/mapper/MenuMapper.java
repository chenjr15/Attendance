package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 菜单项表，自己和自己关联形成的多级菜单 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface MenuMapper extends MyBaseMapper<Menu> {
  @Override
  @Select("SELECT 1 FROM menu WHERE id=#{id} limit 1 ")
  Optional<Boolean> exists(long id);

  @Select("SELECT * FROM menu WHERE parent_id=#{menuId}")
  List<Menu> getChildren(long menuId);

  @Select("SELECT count(id) FROM menu WHERE parent_id=#{parent_id} limit 1")
  int childrenCount(long parent_id);
}
