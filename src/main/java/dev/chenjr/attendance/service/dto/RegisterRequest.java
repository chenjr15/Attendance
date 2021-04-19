package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Data
public class RegisterRequest {

    @Schema(name = "登录名")
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_-]{3,15}", message = "大小字母小写字母开头后面可以包含下划线")
    private String loginName;

    @Schema(description = "真实姓名")
    @Size(min = 2, max = 20)
    private String realName;
    @Email
    @Nullable
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    @Schema(required = true)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;

    private String password;
    @NotNull
    @Pattern(regexp = "\\d{4,6}", message = "only 4-6 digits allowed")
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
