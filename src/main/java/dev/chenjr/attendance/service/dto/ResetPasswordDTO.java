package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.Password;
import dev.chenjr.attendance.service.dto.validation.PasswordValidator;
import dev.chenjr.attendance.service.dto.validation.SmsCode;
import dev.chenjr.attendance.service.dto.validation.SmsCodeValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {
    @Password
    @Schema(required = true, description = "密码", example = "my_password", pattern = PasswordValidator.RE_PASSWORD)
    private String password;

    @SmsCode
    @Schema(required = true, description = "短信验证码", pattern = SmsCodeValidator.RE_SMS_CODE)
    private String smsCode;
}
