package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.LoginName;
import dev.chenjr.attendance.service.dto.validation.Password;
import dev.chenjr.attendance.service.dto.validation.PhoneNumber;
import dev.chenjr.attendance.service.dto.validation.SmsCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Schema(description = "登录名")
    @LoginName
    private String loginName;

    @Schema(description = "真实姓名")
    @Size(min = 2, max = 20)
    private String realName;
    @Email
    @Nullable
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @NotBlank
    @PhoneNumber
    @Schema(required = true)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;

    @Password
    @Schema(description = "用户密码")
    private String password;
    
    @SmsCode
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
