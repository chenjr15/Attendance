package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.entity.UserCourse;
import dev.chenjr.attendance.dao.mapper.CourseMapper;
import dev.chenjr.attendance.dao.mapper.UserCourseMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
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

@Service
@Slf4j
public class CourseService extends ServiceImpl<CourseMapper, Course> implements ICourseService {
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
    public PageWrapper<Course> getStudentElectedCourse(long studentUid, long curPage, long pageSize) {
        // 先获学生选的课的id
        QueryWrapper<UserCourse> userCourseQuery = new QueryWrapper<UserCourse>()
                .eq("user_id", studentUid)
                .select("course_id");
        Page<UserCourse> userCoursePage = userCourseMapper.selectPage(new Page<>(curPage, pageSize), userCourseQuery);
        List<UserCourse> userCourses = userCoursePage.getRecords();
        if (userCourses.size() == 0) {
            return new PageWrapper<>();
        }
        log.info("" + userCoursePage.getRecords());
        // 获取课程详情
        Set<Long> courseIDSet = userCourses
                .stream()
                .map(UserCourse::getCourseId)
                .collect(Collectors.toSet());

        QueryWrapper<Course> courseQuery = new QueryWrapper<Course>().in("id", courseIDSet);

        List<Course> userElectedCourse = courseMapper.selectList(courseQuery);
        return PageWrapper.fromList(userCoursePage, userElectedCourse);
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
     */
    @Override
    @Transactional
    public void joinCourse(long uid, long courseId) {
        log.info("!joinCourse!: uid: {} course:{} ", uid, courseId);
        boolean elected = this.elected(uid, courseId);
        if (elected) {
            throw new HttpStatusException(HttpStatus.ALREADY_REPORTED, "已经加入该课程了！");
        }
        UserCourse userCourse = new UserCourse();
        userCourse.setCourseId(courseId);
        userCourse.setUserId(uid);
        userCourse.setCreator(uid);
        userCourse.setCharacterType(0);
        int insert = userCourseMapper.insert(userCourse);
        if (insert != 1) {
            throw new SuperException("加入课程失败！未知异常");
        }


    }

    /**
     * 创建班课
     *
     * @param creator   创建者
     * @param courseDTO 课程信息
     * @return 创建结果
     */
    @Override
    public CourseDTO createCourse(User creator, CourseDTO courseDTO) {
        Course course = dto2Course(courseDTO);
        course.setCode(RandomUtil.randomString(10));
        course.createBy(creator.getId());
        courseMapper.insert(course);
        return getCourseInfo(course.getId());
    }

    /**
     * 创建课程
     *
     * @param courseID 要删除的课程
     * @param actor    删除者
     */
    @Override
    public void deleteCourse(long courseID, User actor) {
        courseMapper.deleteById(courseID);
    }

    /**
     * 退出课程
     *
     * @param uid      用户id
     * @param courseId 课程id
     */
    @Override
    public void quitCourse(long uid, long courseId) {
        userCourseMapper.quit(uid, courseId);
    }

    @Override
    public PageWrapper<CourseDTO> listAllCourse(long curPage, long pageSize) {
        Page<Course> coursePage = new Page<>(curPage, pageSize);
        coursePage = courseMapper.selectPage(coursePage, null);
        List<CourseDTO> dtoList = new ArrayList<>(coursePage.getRecords().size());
        for (Course record : coursePage.getRecords()) {
            dtoList.add(course2DTO(record));
        }
        return PageWrapper.fromList(coursePage, dtoList);
    }


    /**
     * 获取指定课程的信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Override
    public CourseDTO getCourseInfo(long id) {
        Course course = courseMapper.selectById(id);
        return course2DTO(course);
    }

    private CourseDTO course2DTO(Course record) {
        CourseDTO dto = new CourseDTO();

        dto.setDescription(record.getDescription());
        dto.setLocation(record.getLocation());
        dto.setName(record.getName());
        dto.setSchoolMajorID(record.getSchoolMajor());
        dto.setSchedule(record.getSchedule());
        dto.setEndTime(record.getEndTime());
        dto.setSemester(record.getSemester());
        dto.setStartTime(record.getStartTime());
        dto.setState(record.getState());
        return dto;
    }

    private Course dto2Course(CourseDTO dto) {
        return new Course("",
                dto.getName(), dto.getDescription(), dto.getState(), dto.getSchedule(), dto.getSemester(),
                dto.getStartTime(), dto.getEndTime(), dto.getLocation(), dto.getSchoolMajorID());
    }
}
