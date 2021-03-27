package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static dev.chenjr.attendance.controller.RestResponse.CODE_WRONG_ARGUMENT;

@RestController
@RequestMapping("/auth/")
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping("/login")
    public RestResponse<?> login(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        if (authService.checkPasswordAndAccount(account, password)){
           return RestResponse.okWithMsg("Login success");
        }
        return new RestResponse<>(CODE_WRONG_ARGUMENT,"Incorrect account or password");
    }

    @GetMapping("/setPassword")
    @ResponseBody
    public RestResponse<?> setPassword(@RequestParam(value = "uid") Integer uid, @RequestParam(value = "password") String password) {
        boolean ok = authService.setAuth(uid, password);
        if (ok){
            return  RestResponse.ok();
        }else {
            return new RestResponse<>(CODE_WRONG_ARGUMENT,"User may not exists.");
        }
    }
}
