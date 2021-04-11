package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterRequest {

    private String loginName;
    private String realName;
    private String email;
    private String phone;
    @Schema(description = "性别")
    private Integer gender;
    private String roles;
    private String password;
}
