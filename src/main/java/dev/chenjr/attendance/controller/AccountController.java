package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.AccountExistsException;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.validation.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 处理单个Account的逻辑
 */
@RestController
@RequestMapping("/account")
@Tag(name = "帐号", description = "账号的CRUD")
public class AccountController {
    @Autowired
    IAccountService accountService;

    @GetMapping("/{account}")
    public RestResponse<?> getAccountInfo() {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{account}")
    public RestResponse<?> lockAccountInfo() {
        return RestResponse.notImplemented();
    }

    @GetMapping("/unique/{account}")
    @Operation(description = "判断账号是否已经存在", responses = {
            @ApiResponse(responseCode = "400", description = "账号已存在"),
            @ApiResponse(responseCode = "200", description = "账号未被占用"),
    })
    public RestResponse<?> checkAccountUnique(@PathVariable @Account String account) {
        boolean exists = accountService.accountExists(account);
        if (exists) {
            throw new AccountExistsException();
        }

        return RestResponse.ok();
    }

}
