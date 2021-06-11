package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Course;
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
public interface CourseMapper extends MyBaseMapper<Course> {
    @Override
    @Select("SELECT 1 FROM course WHERE id=#{id} limit 1 ")
    Boolean exists(@Param("id") long id);


}
