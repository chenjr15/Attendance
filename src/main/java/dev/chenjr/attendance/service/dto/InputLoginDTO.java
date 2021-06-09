package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InputLoginDTO {
    @Account
    @NotBlank
    @Schema(required = true, description = "账号: 用户名、邮箱、手机号等", example = "user1", pattern = AccountValidator.RE_ACCOUNT)
    private String account;
    
    @Password
    @Schema(required = true, description = "密码", example = "my_password", pattern = PasswordValidator.RE_PASSWORD)
    private String password;

    @SmsCode
    @Schema(required = true, description = "短信验证码", pattern = SmsCodeValidator.RE_SMS_CODE)
    private String smsCode;
}
