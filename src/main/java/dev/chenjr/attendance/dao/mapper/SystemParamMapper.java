package dev.chenjr.attendance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.chenjr.attendance.dao.entity.SystemParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统参数表关联接口
 * <p>
 * * @author chenjr
 * * @since 2021-06-06
 */
public interface SystemParamMapper extends BaseMapper<SystemParam> {
    @Select("SELECT * FROM system_param WHERE param_code = #{paramCode}")
    SystemParam getByParamCode(@Param("paramCode") String paramCode);

    @Delete("DELETE  FROM system_param WHERE param_code = #{paramCode}")
    SystemParam deleteByParamCode(@Param("paramCode") String paramCode);
}
