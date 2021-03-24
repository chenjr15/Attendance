package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        return authService.checkPasswordAndAccount(account, password).toString();
    }

    @GetMapping("/setPassword")
    public String setPassword(@RequestParam(value = "uid") Integer uid, @RequestParam(value = "password") String password) {
        authService.setAuth(uid,password);
        return  "OK";
    }
}
