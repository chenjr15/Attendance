package dev.chenjr.attendance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Course;
import dev.chenjr.attendance.service.dto.CourseDTO;
import dev.chenjr.attendance.service.dto.MyUserDetail;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.impl.AccountService;
import dev.chenjr.attendance.service.impl.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(originPatterns = "*")
@Tag(name = "课程", description = "选课、退课、已选课程、课程编辑")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/")
    @Operation(description = "获取所有的班课")
    @ResponseBody()
    public RestResponse<List<Course>> getAllCourse(@RequestParam(defaultValue = "0") long curPage, @RequestParam(defaultValue = "10") long pageSize) {
        Page<Course> page = courseService.page(new Page<>(curPage, pageSize));
        List<Course> records = page.getRecords();
        return RestResponse.okWithData(records);
    }

    @GetMapping("/student/{uid}")
    @Operation(description = "获取某个学生加入的所有班课")
    public RestResponse<List<Course>> getStudentElectedCourse(@PathVariable long uid, @RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize) {

        // TODO 权限校验
        List<Course> records = courseService.getStudentElectedCourse(uid, curPage, pageSize);
        return RestResponse.okWithData(records);
    }

    @PostMapping("/student/{uid}")
    @Operation(description = "学生加入班课")
    public RestResponse<?> studentElectCourse(@PathVariable long uid, @RequestBody long courseId) {

        // TODO 权限校验
        return courseService.joinCourse(uid, courseId);
    }

    @PostMapping("/")
    @Operation(description = "创建课程")
    public RestResponse<?> createCourse(@RequestBody CourseDTO courseDTO) {

        MyUserDetail user = accountService.currentUserDetail();
        // TODO 权限校验
        return courseService.createCourse(user, courseDTO);
    }

    @DeleteMapping("/{courseId}")
    @Operation(description = "删除课程")
    public RestResponse<?> deleteCourse(@PathVariable Long courseID) {

        MyUserDetail user = accountService.currentUserDetail();
        courseService.deleteCourse(courseID, user);
        // TODO 权限校验
        return RestResponse.ok();
    }


}
