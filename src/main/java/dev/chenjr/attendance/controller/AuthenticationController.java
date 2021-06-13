package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.LoginDTO;
import dev.chenjr.attendance.service.dto.ResetPasswordDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.TokenUidDTO;
import dev.chenjr.attendance.service.dto.group.ForgetPasswordGroup;
import dev.chenjr.attendance.service.dto.group.ResetPasswordGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "认证", description = "身份认证相关接口,Authentication(who you are), Token 刷新")
public class AuthenticationController {
    @Autowired
    IAccountService authenticationService;

    @Autowired
    IAccountService accountService;

    @Autowired
    ISmsService smsService;

    @Autowired
    IUserService userService;

    // @SecurityRequirements() 传入空数组清空全局的认证设定
    @PostMapping("/login")
    @SecurityRequirements
    @Operation(description = "这个方法用于登录验证后返回token和uid")
    public RestResponse<TokenUidDTO> login(@RequestBody @Validated LoginDTO request) {
        // 尝试登录
        TokenUidDTO tokenUidDTO = authenticationService.loginAndCreateToken(request);
        log.info("Login, return uid:" + tokenUidDTO.getUid() + tokenUidDTO.toString());
        return RestResponse.okWithData(tokenUidDTO);
    }

    @GetMapping("/token")
    @Operation(description = "获取新的token")
    public RestResponse<TokenUidDTO> refreshToken(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestParam(defaultValue = "false") Boolean longTerm
    ) {
        // 尝试登录
        TokenUidDTO tokenUidDTO = new TokenUidDTO();
        tokenUidDTO.setToken(authenticationService.createToken(user, longTerm));
        tokenUidDTO.setUid(user.getId());
        log.info("refresh, return uid:" + tokenUidDTO.getUid() + tokenUidDTO.toString());
        return RestResponse.okWithData(tokenUidDTO);
    }


    // 暂时不需要logout, 客户端直接将token销毁即可
    @PostMapping("/logout")
    public RestResponse<?> logout(@RequestBody @Validated LoginDTO request) {
        return RestResponse.notImplemented();
    }

    /**
     * 修改密码，放到这里，因为这个是对用户的层面的，要对多个account进行修改
     *
     * @param user             当前登陆的用户
     * @param resetPasswordDTO 重设密码dto
     * @return 设置结果
     */
    @PutMapping("/password")
    @Operation(description = "修改**当前**用户密码，_要求先获取短信验证码_，type:`reset_password`, *这里的手机号可以不填*")
    public RestResponse<?> setPassword(
            @AuthenticationPrincipal @Parameter(hidden = true) User user,
            @RequestBody @Validated({ResetPasswordGroup.class, Default.class}) ResetPasswordDTO resetPasswordDTO
    ) {
        Long uid = user.getId();
        accountService.setUserPasswordWithSmsCode(user, resetPasswordDTO.getPassword(), resetPasswordDTO.getSmsCode());
        return RestResponse.ok();
    }

    @PostMapping("/password")
    @SecurityRequirements
    @Operation(description = "修改**指定**用户密码/忘记密码，_要求先获取短信验证码_，type:`reset_password`")
    public RestResponse<?> forgetPassword(
            @RequestBody @Validated({ForgetPasswordGroup.class, Default.class}) ResetPasswordDTO resetPasswordDTO
    ) {
        User user = userService.getUserByAccount(resetPasswordDTO.getPhone());

        accountService.setUserPasswordWithSmsCode(user, resetPasswordDTO.getPassword(), resetPasswordDTO.getSmsCode());

        return RestResponse.ok();
    }
}


