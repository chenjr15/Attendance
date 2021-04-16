package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.dto.InputLoginDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.TokenUidDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    IAccountService authenticationService;

    @Autowired
    IAccountService accountService;


    @PostMapping("/login")
    @Operation(description = "这个方法用于在登录后登录验证后返回token和uid")
    @ResponseBody
    public RestResponse<TokenUidDTO> login(@RequestBody @Validated InputLoginDTO request) {
        // 尝试登录
        TokenUidDTO tokenUidDTO = authenticationService.loginAndCreateToken(request);
        return RestResponse.okWithData(tokenUidDTO);
    }


    // 暂时不需要logout, 客户端直接将token销毁即可
    @PostMapping("/logout")
    @ResponseBody
    public RestResponse<?> logout(@RequestBody @Validated InputLoginDTO request) {
        return RestResponse.notImplemented();
    }

    /**
     * 修改密码，放到这里，因为这个是对用户的层面的，要对多个account进行修改
     *
     * @param uid      user id
     * @param password 密码
     * @return 设置结果
     */
    @PatchMapping("/password/{uid}")
    @ResponseBody
    public RestResponse<?> setPassword(@PathVariable Integer uid, @RequestBody String password) {
        boolean ok = authenticationService.setUserPassword(uid, password);
        if (ok) {
            return RestResponse.ok();
        } else {
            return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), "User may not exists.");
        }
    }
}
