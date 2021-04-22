package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.PhoneNumber;
import dev.chenjr.attendance.service.dto.validation.SmsCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class InputModifyUserDTO {
    @Schema(description = "真实姓名")
    private String realName;
    @Email
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @PhoneNumber
    @Schema(required = true)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;
    @Schema(required = true)
    private String password;
    @SmsCode
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
