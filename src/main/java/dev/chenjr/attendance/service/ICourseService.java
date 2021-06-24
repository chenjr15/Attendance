package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 课程相关业务支持服务， 包含开课、选课、签到、签到日志、经验值查询功能等
 */
public interface ICourseService extends IService {


    /**
     * 学生加入班课(通过课程代码)
     *
     * @param uid        学生id
     * @param courseCode 课程id
     */
    void joinCourse(long uid, String courseCode);

    /**
     * 创建班课
     *
     * @param creator   创建者
     * @param courseDTO 课程信息
     * @return 创建结果
     */
    CourseDTO createCourse(User creator, CourseDTO courseDTO);

    /**
     * 创建课程
     *
     * @param courseID 要删除的课程
     * @param actor    删除者
     */
    void deleteCourse(long courseID, User actor);

    /**
     * 退出课程(通过课程id)
     *
     * @param uid      用户id
     * @param courseId 课程id
     */
    void quitCourse(long uid, long courseId);

    /**
     * 获取所有课程
     *
     * @param pageSort 筛选排序分页
     * @return 分页后的课程
     */
    PageWrapper<CourseDTO> listAllCourse(PageSort pageSort);

    /**
     * 通过课程ID 获取指定课程的信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    CourseDTO getCourseById(long id);

    /**
     * 通过课程代码获取指定课程的信息
     *
     * @param code 课程代码
     * @return 课程信息
     */
    CourseDTO getCourseByCode(String code);

    /**
     * 获取某个学生的选课信息
     *
     * @param pageSort 分页&筛选&排序
     * @return 选课列表
     */
    PageWrapper<CourseDTO> listElectedCourses(long uid, PageSort pageSort);

    /**
     * 选择某个课程的所有学生
     *
     * @param courseId 课程id
     * @param pageSort 分页&筛选&排序
     * @return 学生列表
     */
    PageWrapper<CourseStudentDTO> getCourseStudentsById(long courseId, PageSort pageSort);

    /**
     * 修改课程信息
     *
     * @param courseDTO 修改的信息
     * @return 修改后的dto
     */
    CourseDTO modifyCourse(CourseDTO courseDTO);

    /**
     * 更新课程封面
     *
     * @param courseId 课程id
     * @param uploaded 上传的文件
     * @return 新头像的url
     */
    String modifyAvatar(Long courseId, MultipartFile uploaded);

    /**
     * 获取某个老师教的课
     *
     * @param uid      老师id
     * @param pageSort 分页&筛选&排序
     * @return 课程列表
     */
    PageWrapper<CourseDTO> listTaughtCourse(long uid, PageSort pageSort);
}
