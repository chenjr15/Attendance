package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Dictionary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface DictionaryMapper extends MyBaseMapper<Dictionary> {
    @Override
    @Select("SELECT 1 FROM dictionary WHERE id=#{id} limit 1 ")
    Boolean exists(@Param("id") long id);

    @Select("SELECT * FROM dictionary WHERE code = #{code}")
    Dictionary getByCode(@Param("code") String code);
}
