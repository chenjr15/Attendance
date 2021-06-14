package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.group.ForgetPasswordGroup;
import dev.chenjr.attendance.service.dto.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {

    // 使用分组要记得加上Default.class:
    // @Validated({ForgetPasswordGroup.class, Default.class})
    @PhoneNumber
    @NotBlank(groups = {ForgetPasswordGroup.class})
    @Schema(description = "手机号，在忘记密码的时候使用")
    private String phone;

    @NotBlank
    @Password
    @Schema(required = true, description = "密码", example = "my_password", pattern = PasswordValidator.RE_PASSWORD)
    private String password;

    @NotBlank
    @SmsCode
    @Schema(required = true, description = "短信验证码", pattern = SmsCodeValidator.RE_SMS_CODE)
    private String smsCode;
}
