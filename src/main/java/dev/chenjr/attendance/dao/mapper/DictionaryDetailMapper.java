package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.DictionaryDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface DictionaryDetailMapper extends MyBaseMapper<DictionaryDetail> {

    @Select("SELECT * FROM dictionary_detail where dictionary_id=#{dictId}")
    List<DictionaryDetail> getByDictId(@Param("dictId") Long dictId);

    @Delete("DELETE FROM dictionary_detail where dictionary_id=#{dictId}")
    void deleteByDictId(@Param("dictId") Long dictId);
}
