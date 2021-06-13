package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.DictionaryDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface DictionaryDetailMapper extends MyBaseMapper<DictionaryDetail> {
    @Override
    @Select("SELECT 1 FROM dictionary_detail WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(@Param("id") long id);

    @Select("SELECT * FROM dictionary_detail where dictionary_id=#{dictId}")
    List<DictionaryDetail> getByDictId(@Param("dictId") Long dictId);

    @Delete("DELETE FROM dictionary_detail where dictionary_id=#{dictId}")
    void deleteByDictId(@Param("dictId") Long dictId);

    @Update("UPDATE dictionary_detail SET default_item=0 WHERE dictionary_id=#{dictId}")
    void unSetDefault(@Param("dictId") long dictId);

    @Update("UPDATE dictionary_detail SET default_item=1 WHERE id=#{id} ")
    void setDefault(@Param("id") long id);
}
