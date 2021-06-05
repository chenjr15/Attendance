package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.entity.UserCourse;
import dev.chenjr.attendance.dao.mapper.CourseMapper;
import dev.chenjr.attendance.dao.mapper.UserCourseMapper;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CourseService extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    public static final RestResponse<?> ELECTED_SUCCESSFULLY = RestResponse.okWithMsg("加入班课成功！");
    public static final RestResponse<?> ALREADY_ELECTED = new RestResponse<>(HttpStatus.ALREADY_REPORTED.value(), "已加入该班课！");
    @Autowired
    UserCourseMapper userCourseMapper;
    @Autowired
    CourseMapper courseMapper;

    /**
     * 获取某个学生的选课信息
     *
     * @param studentUid 学生id
     * @param curPage    当前分页
     * @param pageSize   分页大小
     * @return 选课列表
     */
    @Override
    public List<Course> getStudentElectedCourse(long studentUid, long curPage, long pageSize) {
        // 先获学生选的课的id
        QueryWrapper<UserCourse> userCourseQuery = new QueryWrapper<UserCourse>()
                .eq("user_id", studentUid)
                .select("course_id");
        Page<UserCourse> userCoursePage = userCourseMapper.selectPage(new Page<>(curPage, pageSize), userCourseQuery);
        List<UserCourse> userCourses = userCoursePage.getRecords();
        if (userCourses.size() == 0) {
            return new ArrayList<>();
        }
        log.info("" + userCoursePage.getRecords());
        Stream<Long> courseIdStream = userCourses.stream().map(UserCourse::getCourseId);
        Set<Long> courseIDSet = courseIdStream.collect(Collectors.toSet());
        QueryWrapper<Course> courseQuery = new QueryWrapper<Course>()
                .in("id", courseIDSet);
        Page<Course> page = page(new Page<>(curPage, pageSize), courseQuery);
        Page<Course> resultPage = courseMapper.selectPage(page, courseQuery);
        return resultPage.getRecords();
    }

    public boolean elected(long uid, long courseId) {
        return userCourseMapper.elected(uid, courseId) != null;
    }

    public boolean courseExists(long courseId) {
        return courseMapper.exists(courseId) != null;
    }

    /**
     * 学生加入班课
     *
     * @param uid      学生id
     * @param courseId 课程id
     * @return 加入结果
     */
    @Override
    @Transactional
    public RestResponse<?> joinCourse(long uid, long courseId) {
        log.info("!joinCourse!: uid: {} course:{} ", uid, courseId);
        boolean elected = this.elected(uid, courseId);
        if (elected) {
            return ALREADY_ELECTED;
        }
        UserCourse userCourse = new UserCourse();
        userCourse.setCourseId(courseId);
        userCourse.setUserId(uid);
        userCourse.setCreator(uid);
        userCourse.setCharacterType(0);
        userCourseMapper.insert(userCourse);

        return ELECTED_SUCCESSFULLY;


    }

    /**
     * 创建班课
     *
     * @param creator   创建者
     * @param courseDTO 课程信息
     * @return 创建结果
     */
    @Override
    public RestResponse<?> createCourse(User creator, CourseDTO courseDTO) {
        Course course = courseDTO.toCourse();
        course.setCode(RandomUtil.randomString(10));
        course.createBy(creator.getId());
        courseMapper.insert(course);
        return null;
    }

    /**
     * 创建课程
     *
     * @param courseID 要删除的课程
     * @param actor    删除者
     * @return 创建结果
     */
    @Override
    public boolean deleteCourse(long courseID, User actor) {
        return false;
    }
}
