package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.University;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 学校信息表，学校-> 院系->专业->班级 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface UniversityMapper extends BaseMapper<University> {

}
