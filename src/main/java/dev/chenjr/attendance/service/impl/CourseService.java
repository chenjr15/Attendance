package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.entity.UserCourse;
import dev.chenjr.attendance.dao.mapper.CheckInLogMapper;
import dev.chenjr.attendance.dao.mapper.CourseMapper;
import dev.chenjr.attendance.dao.mapper.UserCourseMapper;
import dev.chenjr.attendance.dao.mapper.UserMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.IOrganizationService;
import dev.chenjr.attendance.service.IStorageService;
import dev.chenjr.attendance.service.dto.*;
import dev.chenjr.attendance.utils.RandomUtil;
import dev.chenjr.attendance.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    UserCourseMapper userCourseMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    IOrganizationService organizationService;
    
    @Autowired
    IStorageService storageService;
    
    @Autowired
    CheckInLogMapper checkInLogMapper;
    
    /**
     * 获取指定课程的信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Override
    public CourseDTO getCourseById(long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw HttpStatusException.notFound("找不到课程！");
        }
        return course2DTO(course);
    }
    
    /**
     * 通过课程代码获取指定课程的信息
     *
     * @param code 课程代码
     * @return 课程信息
     */
    @Override
    public CourseDTO getCourseByCode(String code) {
        code = code.toLowerCase(Locale.ROOT);
        Course course = courseMapper.getByCode(code);
        if (course == null) {
            throw HttpStatusException.notFound("找不到课程！");
        }
        return course2DTO(course);
    }
    
    /**
     * 获取所有课程
     *
     * @param pageSort 筛选排序分页
     * @return 分页后的课程
     */
    @Override
    public PageWrapper<CourseDTO> listAllCourse(PageSort pageSort) {
        Page<Course> coursePage = pageSort.getPage();
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper = pageSort.buildQueryWrapper(queryWrapper);
        coursePage = courseMapper.selectPage(coursePage, queryWrapper);
        List<CourseDTO> dtoList = coursePage.getRecords().stream().map(this::course2DTO).collect(Collectors.toList());
        return PageWrapper.fromList(coursePage, dtoList);
    }
    
    /**
     * 获取某个学生的选课信息
     *
     * @param uid      学生uid
     * @param pageSort 分页&筛选&排序
     * @return 选课列表
     */
    @Override
    public PageWrapper<CourseDTO> listElectedCourses(long uid, PageSort pageSort) {
        // 先获学生选的课的id
        QueryWrapper<UserCourse> userCourseQuery = new QueryWrapper<UserCourse>()
                .eq("user_id", uid)
                .select("course_id");
        Page<UserCourse> userCoursePage = userCourseMapper.selectPage(pageSort.getPage(), userCourseQuery);
        List<UserCourse> userCourses = userCoursePage.getRecords();
        if (userCourses.size() == 0) {
            return new PageWrapper<>();
        }
        log.debug("" + userCoursePage.getRecords());
        // 获取课程详情
        Set<Long> courseIDSet = userCourses
                .stream()
                .map(UserCourse::getCourseId)
                .collect(Collectors.toSet());
        
        QueryWrapper<Course> courseQuery = new QueryWrapper<Course>().in("id", courseIDSet);
        
        List<Course> userElectedCourse = courseMapper.selectList(courseQuery);
        List<CourseDTO> dtoList = userElectedCourse.stream().map(this::course2DTO).collect(Collectors.toList());
        return PageWrapper.fromList(userCoursePage, dtoList);
    }
    
    /**
     * 选择某个课程的所有学生
     *
     * @param courseId 课程id
     * @param pageSort 分页&筛选&排序
     * @return 学生列表
     */
    @Override
    public PageWrapper<CourseStudentDTO> getCourseStudentsById(long courseId, PageSort pageSort) {
        
        Page<Long> idPage = userCourseMapper.listElected(courseId, pageSort.getPage());
        List<CourseStudentDTO> students = new ArrayList<>(idPage.getRecords().size());
        for (Long userId : idPage.getRecords()) {
            CourseStudentDTO student = userService.getStudent(userId);
            // 计算经验值
            int exp = checkInLogMapper.totalExpInCourse(courseId, userId);
            student.setExperience(exp);
            students.add(student);
        }
        return PageWrapper.fromList(idPage, students);
    }
    
    /**
     * 修改课程信息
     *
     * @param courseDTO 修改的信息
     * @return 修改后的dto
     */
    @Override
    public CourseDTO modifyCourse(CourseDTO courseDTO) {
        Optional<Boolean> exists = courseMapper.exists(courseDTO.getId());
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("课程id不存在！");
        }
        // 如果设置结课，则同时设置结课时间
        if (courseDTO.getState() == Course.STATE_ENDED) {
            courseDTO.setEndTime(LocalDateTime.now());
        }
        Course toModify = dto2Entity(courseDTO);
        courseMapper.updateById(toModify);
        return this.getCourseById(courseDTO.getId());
    }
    
    /**
     * 更新课程封面
     *
     * @param courseId 课程id
     * @param uploaded 上传的文件
     * @return 新头像的url
     */
    @Override
    public String modifyAvatar(Long courseId, MultipartFile uploaded) {
        Optional<Boolean> exists = courseMapper.exists(courseId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        String filename = storageService.storeFile(uploaded);
        
        Course course = new Course();
        course.setId(courseId);
        course.setAvatar(filename);
        courseMapper.updateById(course);
        return storageService.getFullUrl(filename);
    }
    
    /**
     * 获取某个老师教的课
     *
     * @param uid      老师id
     * @param pageSort 分页&筛选&排序
     * @return 课程列表
     */
    @Override
    public PageWrapper<CourseDTO> listTaughtCourse(long uid, PageSort pageSort) {
        
        // 获取老师创建的课的id
        QueryWrapper<Course> courseQuery = new QueryWrapper<Course>().eq("creator", uid);
        Page<Course> coursePage = courseMapper.selectPage(pageSort.getPage(), courseQuery);
        List<Course> userElectedCourse = coursePage.getRecords();
        List<CourseDTO> dtoList = userElectedCourse.stream().map(this::course2DTO).collect(Collectors.toList());
        return PageWrapper.fromList(coursePage, dtoList);
    }
    
    
    public boolean elected(long uid, long courseId) {
        return userCourseMapper.isElected(uid, courseId) != null;
    }
    
    public boolean courseExists(long courseId) {
        return courseMapper.exists(courseId).isPresent();
    }
    
    /**
     * 学生加入班课
     *
     * @param uid      学生id
     * @param courseId 课程id
     */
    @Transactional
    public void joinCourse(long uid, long courseId) {
        Optional<Boolean> exists = courseMapper.exists(courseId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("课程不存在！");
        }
        exists = userMapper.exists(uid);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("用户不存在！");
        }
        log.debug("!joinCourse!: uid: {} course:{} ", uid, courseId);
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
     * 学生加入班课(通过课程代码)
     *
     * @param uid        学生id
     * @param courseCode 课程id
     */
    @Override
    public void joinCourse(long uid, String courseCode) {
        Course course = courseMapper.getByCode(courseCode);
        if (course == null) {
            throw HttpStatusException.notFound("加入失败: 课程代码" + courseCode + "不存在！");
        }
        if (!course.canJoin()) {
            throw HttpStatusException.badRequest("加入失败: " + getStateMessage(course.getState()));
        }
        joinCourse(uid, course.getId());
    }
    
    String getStateMessage(Integer state) {
        String msg;
        switch (state) {
            case Course.STATE_FORBIDDEN_JOIN:
                msg = "禁止加入";
                break;
            case Course.STATE_ENDED:
                msg = "已结课";
                break;
            default:
                msg = "开课中";
        }
        return msg;
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
        if (courseDTO.getStartTime() == null) {
            courseDTO.setStartTime(LocalDateTime.now());
        }
        Course course = dto2Entity(courseDTO);
        String code = RandomUtil.randomString(6);
        while (courseMapper.codeExists(code) != null) {
            code = RandomUtil.randomString(6);
        }
        course.setCode(code);
        course.createBy(creator.getId());
        courseMapper.insert(course);
        return getCourseById(course.getId());
    }
    
    /**
     * 创建课程
     *
     * @param courseID 要删除的课程
     * @param actor    删除者
     */
    @Override
    public void deleteCourse(long courseID, User actor) {
        userCourseMapper.deleteByCourseId(courseID);
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
    
    
    private CourseDTO course2DTO(Course entity) {
        CourseDTO dto = new CourseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode().toUpperCase(Locale.ROOT));
        dto.setDescription(entity.getDescription());
        dto.setCourseClass(entity.getCourseClass());
        dto.setName(entity.getName());
        
        dto.setSchedule(entity.getSchedule());
        dto.setEndTime(entity.getEndTime());
        dto.setSemester(entity.getSemester());
        dto.setStartTime(entity.getStartTime());
        if (dto.getStartTime() == null) {
            dto.setStartTime(entity.getCreateTime());
        }
        dto.setState(entity.getState());
        dto.setStateName(getStateMessage(entity.getState()));
        dto.setAvatar(storageService.getFullUrl(entity.getAvatar()));
        Long teacher = entity.getCreator();
        dto.setTeacherId(teacher);
        dto.setTeacherName(userService.getRealNameById(teacher));
        
        Long schoolMajorID = entity.getSchoolMajor();
        if (schoolMajorID != null) {
            dto.setSchoolMajorID(schoolMajorID);
            dto.setSchoolMajorName("UNKNOWN");
            OrganizationDTO schoolDto = organizationService.fetchItSelf(schoolMajorID);
            if (schoolDto != null) {
                String joined = StringUtil.join(schoolDto.getParents(), schoolDto.getName());
                dto.setSchoolMajorName(joined);
            }
            
        }
        return dto;
    }
    
    
    private Course dto2Entity(CourseDTO courseDTO) {
        Course entity = new Course();
        entity.setId(courseDTO.getId());
        entity.setName(courseDTO.getName());
        entity.setCode(courseDTO.getCode().toLowerCase(Locale.ROOT));
        entity.setCourseClass(courseDTO.getCourseClass());
        entity.setSchedule(courseDTO.getSchedule());
        entity.setDescription(courseDTO.getDescription());
        entity.setEndTime(courseDTO.getEndTime());
        entity.setSchoolMajor(courseDTO.getSchoolMajorID());
        entity.setStartTime(courseDTO.getStartTime());
        entity.setState(courseDTO.getState());
        entity.setSemester(courseDTO.getSemester());
        entity.setAvatar(courseDTO.getAvatar());
        return entity;
    }
}
