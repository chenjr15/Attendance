package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {

    @Schema(required = true)
    @NotBlank
    @Length(min = 6, max = 20)
    private String loginName;

    @Schema(description = "真实姓名")
    private String realName;
    @Email
    @Nullable
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @NotBlank
    @Length(min = 4, max = 20)
    @Schema(required = true)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;
    @Length(min = 6, max = 20)
    @Schema(required = true)
    private String password;
    @Length(min = 6, max = 6)
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
