package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Schema(description = "登录名", pattern = LoginNameValidator.RE_LOGIN_NAME)
    @LoginName
    private String loginName;

    @Schema(description = "真实姓名")
    @Size(min = 2, max = 20)
    private String realName;
    @Email
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @NotBlank
    @PhoneNumber
    @Schema(required = true, pattern = PhoneNumberValidator.RE_PHONE_NUMBER)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;

    @Password
    @Schema(description = "用户密码", pattern = PasswordValidator.RE_PASSWORD)
    private String password;

    @SmsCode
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
