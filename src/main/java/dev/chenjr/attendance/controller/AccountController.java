package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/{account}")
    @ResponseBody
    public RestResponse<?> getAccountInfo() {
        return RestResponse.notImplemented();
    }
}
