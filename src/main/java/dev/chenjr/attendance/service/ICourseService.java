package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.RestResponse;

import java.util.List;

/**
 * 课程相关业务支持服务， 包含开课、选课、签到、签到日志、经验值查询功能等
 */
public interface ICourseService extends IService {

    /**
     * 获取某个学生的选课信息
     *
     * @param studentUid 学生id
     * @param curPage    当前分页
     * @param pageSize   分页大小
     * @return 选课列表
     */
    List<Course> getStudentElectedCourse(long studentUid, long curPage, long pageSize);

    /**
     * 学生加入班课
     *
     * @param uid      学生id
     * @param courseId 课程id
     * @return 加入结果
     */
    RestResponse<?> joinCourse(long uid, long courseId);

    /**
     * 创建班课
     *
     * @param creator   创建者
     * @param courseDTO 课程信息
     * @return 创建结果
     */
    RestResponse<?> createCourse(User creator, CourseDTO courseDTO);

    /**
     * 创建课程
     *
     * @param courseID 要删除的课程
     * @param actor    删除者
     * @return 创建结果
     */
    boolean deleteCourse(long courseID, User actor);
}
