package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.MyUserDetail;
import dev.chenjr.attendance.service.dto.RestResponse;

import java.util.List;

/**
 * 课程相关业务支持服务， 包含开课、选课、签到、签到日志、经验值查询功能等
 */
public interface ICourseService extends IService {

    List<Course> getStudentElectedCourse(long uid, long curPage, long pageSize);

    RestResponse<?> joinCourse(long uid, long courseId);

    RestResponse<?> createCourse(MyUserDetail creator, CourseDTO courseDTO);

    boolean deleteCourse(long courseID, MyUserDetail actor);
}
