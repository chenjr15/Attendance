package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.Account;
import dev.chenjr.attendance.service.dto.validation.Password;
import dev.chenjr.attendance.service.dto.validation.SmsCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import reactor.util.annotation.Nullable;

@Data
public class InputLoginDTO {
    @Account
    @Schema(required = true, description = "账号: 用户名、邮箱、手机号等", example = "user1")
    private String account;
    @Password
    @Schema(required = true, description = "密码", example = "my_password")
    private String password;

    @Nullable
    @SmsCode
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
