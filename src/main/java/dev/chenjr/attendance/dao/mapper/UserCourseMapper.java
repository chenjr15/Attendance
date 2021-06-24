package dev.chenjr.attendance.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.chenjr.attendance.dao.entity.UserCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户选课表，记录用户和所选课程，包含，如果类型是学生，则说明该学生选择了该课程，如果类型是老师，则说明该用户是该课程的老师 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface UserCourseMapper extends MyBaseMapper<UserCourse> {
    @Select("SELECT 1 FROM user_course WHERE user_id = #{uid} and course_id=#{courseId} limit 1 ")
    Boolean isElected(long uid, long courseId);

    @Select("SELECT user_id FROM user_course WHERE course_id=#{courseId} ")
    <E extends IPage<Long>> E listElected(long courseId, E page);

    @Select("SELECT user_id FROM user_course WHERE course_id=#{courseId} ")
    List<Long> listElected(long courseId);

    @Delete("DELETE  FROM user_course WHERE user_id = #{uid} and course_id=#{courseId}")
    int quit(@Param("uid") long uid, @Param("courseId") long courseId);

    @Delete("DELETE  FROM user_course WHERE  course_id=#{courseId}")
    int deleteByCourseId(long courseID);
}
