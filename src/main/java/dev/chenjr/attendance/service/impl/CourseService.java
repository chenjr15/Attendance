package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.UserCourse;
import dev.chenjr.attendance.dao.mapper.CourseMapper;
import dev.chenjr.attendance.dao.mapper.UserCourseMapper;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.MyUserDetail;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CourseService extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    public static final RestResponse<?> ELECTED_SUCCESSFULLY = RestResponse.okWithMsg("Elected successfully");
    public static final RestResponse<?> ALREADY_ELECTED = new RestResponse<>(HttpStatus.ALREADY_REPORTED.value(), "Elected successfully");
    @Autowired
    UserCourseMapper userCourseMapper;
    @Autowired
    CourseMapper courseMapper;

    @Override
    public List<Course> getStudentElectedCourse(long uid, long curPage, long pageSize) {
        QueryWrapper<UserCourse> userCourseQuery = new QueryWrapper<UserCourse>()
                .eq("user_id", uid)
                .select("course_id");
        Page<UserCourse> userCoursePage = userCourseMapper.selectPage(new Page<>(curPage, pageSize), userCourseQuery);
        List<UserCourse> userCourses = userCoursePage.getRecords();
        Stream<Long> courseIdStream = userCourses.stream().map(UserCourse::getCourseId);
        Set<Long> courseIDSet = courseIdStream.collect(Collectors.toSet());
        QueryWrapper<Course> courseQuery = new QueryWrapper<Course>()
                .eq("user_id", uid)
                .in("id", courseIDSet);
        Page<Course> page = page(new Page<>(curPage, pageSize), courseQuery);
        return page.getRecords();
    }

    public boolean elected(long uid, long courseId) {
        return userCourseMapper.elected(uid, courseId) != null;
    }

    public boolean courseExists(long courseId) {
        return courseMapper.exists(courseId) != null;
    }


    @Override
    public RestResponse<?> joinCourse(long uid, long courseId) {

        boolean elected = this.elected(uid, courseId);
        if (elected) {
            return ALREADY_ELECTED;
        }
        UserCourse userCourse = new UserCourse();
        userCourse.setCourseId(courseId);
        userCourse.setUserId(uid);
        userCourse.setCreator(uid);
        userCourseMapper.insert(userCourse);

        return ELECTED_SUCCESSFULLY;


    }

    @Override
    public RestResponse<?> createCourse(MyUserDetail creator, CourseDTO courseDTO) {
        Course course = courseDTO.toCourse();
        course.setCreator(creator.getUid());
        course.setUpdater(creator.getUid());
        courseMapper.insert(course);
        return null;
    }

    @Override
    public boolean deleteCourse(long courseID, MyUserDetail actor) {
        return false;
    }
}
