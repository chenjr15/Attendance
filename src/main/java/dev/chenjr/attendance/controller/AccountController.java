package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 处理单个Account的逻辑
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/{account}")
    @ResponseBody
    public RestResponse<?> getAccountInfo() {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{account}")
    @ResponseBody
    public RestResponse<?> lockAccountInfo() {
        return RestResponse.notImplemented();
    }
}
