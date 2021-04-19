package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 处理单个Account的逻辑
 */
@RestController
@RequestMapping("/account")
@Tag(name = "帐号", description = "账号的CRUD")
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
