package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.ModifyUserRequest;
import dev.chenjr.attendance.service.dto.RegisterRequest;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.impl.AccountService;
import dev.chenjr.attendance.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    AccountService authenticationService;
    @Autowired
    UserService userService;


    @PostMapping("/signup")
    @Operation(description = "注册")
    @ResponseBody
    public RestResponse<String> register(@RequestBody RegisterRequest request) {
        // 尝试创建Token，失败会报错
        String token = "";
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
