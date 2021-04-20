package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理单个Account的逻辑
 */
@RestController
@RequestMapping("/account")
@Tag(name = "帐号", description = "账号的CRUD")
public class AccountController {

    @GetMapping("/{account}")
    public RestResponse<?> getAccountInfo() {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{account}")
    public RestResponse<?> lockAccountInfo() {
        return RestResponse.notImplemented();
    }
}
