package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.AuthenticationService;
import dev.chenjr.attendance.service.dto.LoginRequest;
import dev.chenjr.attendance.service.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;


    // 这个方法用于在登录后登录验证后返回token
    @PostMapping("/login")
    @ResponseBody
    public RestResponse<String> login(@RequestBody LoginRequest request) {
        // 尝试登录
        String token = authenticationService.createLoginToken(request);
        return RestResponse.okWithData(token);
    }


    @PutMapping("/password/{uid}")
    @ResponseBody
    public RestResponse<?> setPassword(@PathVariable Integer uid, @RequestBody String password) {
        boolean ok = authenticationService.setAuth(uid, password);
        if (ok) {
            return RestResponse.ok();
        } else {
            return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), "User may not exists.");
        }
    }
}
