package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/courses")
@Tag(name = "课程", description = "选课、退课、已选课程、课程编辑")
public class CourseController {
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IAccountService accountService;

    @GetMapping(value = "/")
    @Operation(description = "获取所有的班课")
    public RestResponse<PageWrapper<CourseDTO>> getAllCourse(
            @RequestParam(defaultValue = "0") long curPage, @RequestParam(defaultValue = "10") long pageSize
    ) {
        PageWrapper<CourseDTO> page = courseService.listAllCourse(curPage, pageSize);
        return RestResponse.okWithData(page);
    }

    @GetMapping("/student/{uid}")
    @Operation(description = "获取某个学生加入的所有班课")
    public RestResponse<PageWrapper<Course>> getStudentElectedCourse(
            @PathVariable long uid,
            @RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize
    ) {

        // TODO 权限校验
        PageWrapper<Course> records = courseService.getStudentElectedCourse(uid, curPage, pageSize);
        return RestResponse.okWithData(records);
    }

    @PostMapping("/student/{uid}/{courseId}")
    @Operation(description = "学生加入班课")
    public RestResponse<?> studentElectCourse(@PathVariable long uid, @PathVariable long courseId) {

//        courseService.joinCourse(uid, courseId);
        // TODO 权限校验 是当前用户或者是该课程的老师或者是管理员
        courseService.joinCourse(uid, courseId);
        return RestResponse.ok();
    }

    @DeleteMapping("/student/{uid}/{courseId}")
    @Operation(description = "学生退出班课")
    public RestResponse<?> studentQuitCourse(@PathVariable long uid, @PathVariable long courseId) {
        // TODO 权限校验 是当前用户或者是该课程的老师或者是管理员
        courseService.quitCourse(uid, courseId);
        return RestResponse.okWithMsg("Quited");
    }

    @PostMapping("/")
    @Operation(description = "创建课程")
    public RestResponse<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {

        User user = accountService.currentUser();
        // TODO 权限校验
        CourseDTO created = courseService.createCourse(user, courseDTO);
        return RestResponse.okWithData(created);
    }

    @DeleteMapping("/{courseId}")
    @Operation(description = "删除课程")
    public RestResponse<?> deleteCourse(@PathVariable Long courseID) {

        User user = accountService.currentUser();
        courseService.deleteCourse(courseID, user);
        // TODO 权限校验
        return RestResponse.ok();
    }


}
