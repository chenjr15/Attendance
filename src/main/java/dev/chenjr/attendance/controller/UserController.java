package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.AuthenticationService;
import dev.chenjr.attendance.service.UserService;
import dev.chenjr.attendance.service.dto.LoginRequest;
import dev.chenjr.attendance.service.dto.ModifyUserRequest;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;


    // TODO 移到Account 下
    @PostMapping("/")
    @Operation(description = "注册")
    @ResponseBody
    public RestResponse<String> register(@RequestBody LoginRequest request) {
        // 尝试创建Token，失败会报错
        String token = authenticationService.createLoginToken(request);
        return RestResponse.okWithData(token);
    }

    @GetMapping("/{uid}")
    @Operation(description = "获取指定用户的信息")
    @ResponseBody
    public RestResponse<?> getUser(@PathVariable Integer uid) {
        User user = userService.getUserById(uid);
        return RestResponse.okWithData(user);
    }

    @DeleteMapping("/{uid}")
    @Operation(description = "删除指定用户")
    @ResponseBody
    public RestResponse<?> deleteUser(@PathVariable Integer uid) {
        return RestResponse.notImplemented();
    }


    @PutMapping("/{uid}")
    @Operation(description = "修改用户信息")
    @ResponseBody
    public RestResponse<?> modifyUser(@PathVariable Integer uid, @RequestBody ModifyUserRequest modifyUserRequest) {
        return RestResponse.notImplemented();
    }
}
