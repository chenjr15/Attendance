package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.ICourseService;
import dev.chenjr.attendance.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/courses")
@Tag(name = "课程", description = "选课、退课、已选课程、课程编辑")
public class CourseController {
    @Autowired
    private ICourseService courseService;
    @Autowired
    private IAccountService accountService;

    @GetMapping(value = "/{courseId}")
    @Operation(description = "获取课程信息(By ID)")
    public RestResponse<CourseDTO> getCourseById(
            @PathVariable long courseId
    ) {
        CourseDTO courseById = courseService.getCourseById(courseId);
        return RestResponse.okWithData(courseById);
    }

    @GetMapping(value = "/code/{courseCode}")
    @Operation(description = "获取课程信息(By Code)")
    public RestResponse<CourseDTO> getCourseByCode(
            @PathVariable String courseCode
    ) {
        CourseDTO course = courseService.getCourseByCode(courseCode);
        return RestResponse.okWithData(course);
    }

    @PutMapping(value = "/{courseId}/state")
    @Operation(description = "设置课程状态：\n" +
            "- `0`: 开课中(默认状态)\n" +
            "- `1`: 设置禁止加入\n" +
            "- `2`: 课程结束\n")
    public RestResponse<CourseDTO> setCourseState(
            @PathVariable long courseId, @RequestBody int state
    ) {
        CourseDTO dto = new CourseDTO();
        dto.setId(courseId);
        dto.setState(state);
        CourseDTO course = courseService.modifyCourse(dto);
        return RestResponse.okWithData(course);
    }

    @GetMapping(value = "/{courseId}/students")
    @Operation(description = "获取课程信息的学生列表(By ID)")
    public RestResponse<PageWrapper<UserDTO>> getCourseStudentsById(
            @PathVariable long courseId,
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<UserDTO> pageWrapper = courseService.getCourseStudentsById(courseId, pageSort);
        return RestResponse.okWithData(pageWrapper);
    }

    @GetMapping(value = "")
    @Operation(description = "获取所有的班课")
    public RestResponse<PageWrapper<CourseDTO>> getAllCourse(
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<CourseDTO> page = courseService.listAllCourse(pageSort);
        return RestResponse.okWithData(page);
    }

    @GetMapping("/joined/{uid}")
    @Operation(description = "获取某个学生加入的所有班课")
    public RestResponse<PageWrapper<CourseDTO>> getStudentElectedCourse(
            @PathVariable long uid,
            @ParameterObject PageSort pageSort
    ) {

        // TODO 权限校验
        PageWrapper<CourseDTO> records = courseService.listElectedCourses(uid, pageSort);
        return RestResponse.okWithData(records);
    }

    @GetMapping("/taught/{uid}")
    @Operation(description = "获取某个老师教的所有班课")
    public RestResponse<PageWrapper<CourseDTO>> getTeachCourse(
            @PathVariable long uid,
            @ParameterObject PageSort pageSort
    ) {

        // TODO 权限校验
        PageWrapper<CourseDTO> records = courseService.listTaughtCourse(uid, pageSort);
        return RestResponse.okWithData(records);
    }

    @PostMapping("/students/{uid}/{courseCode}")
    @Operation(description = "学生加入班课(通过Code，其他都用id)")
    public RestResponse<?> studentElectCourse(@PathVariable long uid, @PathVariable String courseCode) {

        // TODO 权限校验 是当前用户或者是该课程的老师或者是管理员
        courseService.joinCourse(uid, courseCode);
        return RestResponse.ok();
    }

    @DeleteMapping("/students/{uid}/{courseId}")
    @Operation(description = "学生退出班课")
    public RestResponse<?> studentQuitCourse(@PathVariable long uid, @PathVariable long courseId) {
        // TODO 权限校验 是当前用户或者是该课程的老师或者是管理员
        courseService.quitCourse(uid, courseId);
        return RestResponse.okWithMsg("Quited");
    }

    @PostMapping("")
    @Operation(description = "创建课程")
    public RestResponse<CourseDTO> createCourse(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody CourseDTO courseDTO
    ) {

        // TODO 权限校验
        CourseDTO created = courseService.createCourse(user, courseDTO);
        return RestResponse.okWithData(created);
    }

    @DeleteMapping("/{courseId}")
    @Operation(description = "删除课程")
    public RestResponse<?> deleteCourse(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @PathVariable Long courseId
    ) {

        courseService.deleteCourse(courseId, user);
        // TODO 权限校验
        return RestResponse.ok();
    }

    @PatchMapping("/{courseId}")
    @Operation(description = "修改课程信息")
    public RestResponse<CourseDTO> modifyCourse(@PathVariable long courseId, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(courseId);
        CourseDTO modified = courseService.modifyCourse(courseDTO);
        return RestResponse.okWithData(modified);
    }

    @PutMapping(value = "/{courseId}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "修改头像（上传）")
    public RestResponse<String> modifyAvatar(
            @PathVariable long courseId,
            @RequestParam("avatar") MultipartFile uploaded
    ) {

        String storeName = courseService.modifyAvatar(courseId, uploaded);
        return RestResponse.okWithData(storeName);
    }
}
