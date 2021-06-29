package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Menu;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
  
  @Select("SELECT * FROM menu WHERE parent_id=#{menuId} AND id!=#{parent_id} ORDER BY order_value")
  List<Menu> getChildren(long menuId);
  
  @Select("SELECT count(id) FROM menu WHERE parent_id=#{parent_id} AND id!=#{parent_id} limit 1")
  int childrenCount(long parent_id);
  
  @Select("SELECT id FROM menu WHERE parent_id=#{menuId} AND id!=#{parent_id} ORDER BY order_value")
  List<Long> getChildrenIds(long menuId);
  
  @Update("<script> UPDATE menu " +
          "SET order_value =   " +
          "<foreach item=\"item\" collection=\"list\" open=\"CASE\" separator=\" \" close=\"END\" > " +
          "   WHEN id = #{item.id} THEN #{item.orderValue}" +
          "</foreach>  " +
          "WHERE id IN " +
          "<foreach item=\"item\" collection=\"list\"  open=\"(\" separator=\",\" close=\")\">" +
          "#{item.id}" +
          " </foreach> " +
          "</script> ")
  void updateOrderBatch(List<Menu> toUpdate);
}
