package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InputModifyUserDTO {
    @Schema(description = "真实姓名")
    private String realName;
    @Schema(description = "用户邮箱，可以不填")
    private String email;
    @Schema(required = true)
    private String phone;
    @Schema(description = "性别", example = "0")
    private Integer gender = 0;
    @Schema(description = "用户角色", example = "ROLE_STUDENT")
    private String roles;
    @Schema(required = true)
    private String password;
    @Schema(required = true, description = "短信验证码")
    private String smsCode;
}
