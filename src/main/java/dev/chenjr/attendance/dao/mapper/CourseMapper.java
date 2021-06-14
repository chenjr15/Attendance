package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

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
    Optional<Boolean> exists(@Param("id") long id);

    /**
     * 通过课程代码获取课程
     *
     * @param courseCode 课程代码
     * @return 课程实体
     */
    @Select("SELECT * FROM course WHERE code=#{code}")
    Course getByCode(@Param("code") String courseCode);

}
