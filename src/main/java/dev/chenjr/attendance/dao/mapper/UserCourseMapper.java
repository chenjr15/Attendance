package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.UserCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户选课表，记录用户和所选课程，包含，如果类型是学生，则说明该学生选择了该课程，如果类型是老师，则说明该用户是该课程的老师 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface UserCourseMapper extends BaseMapper<UserCourse> {

}
