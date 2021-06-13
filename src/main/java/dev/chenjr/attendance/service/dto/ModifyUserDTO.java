package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.Password;
import dev.chenjr.attendance.service.dto.validation.PasswordValidator;
import dev.chenjr.attendance.service.dto.validation.PhoneNumber;
import dev.chenjr.attendance.service.dto.validation.PhoneNumberValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ModifyUserDTO {
    
    @Schema(description = "真实姓名")
    private String realName;

    @Email
    @Schema(description = "用户邮箱，可以不填")
    private String email;

    @PhoneNumber
    @Schema(required = true, pattern = PhoneNumberValidator.RE_PHONE_NUMBER)
    private String phone;

    @Schema(description = "性别", example = "0")
    private Integer gender = 0;

    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;

    @Password
    @Schema(required = true, pattern = PasswordValidator.RE_PASSWORD)
    private String password;
//    @SmsCode
//    @Schema(required = true, description = "短信验证码")
//    private String smsCode;
}
