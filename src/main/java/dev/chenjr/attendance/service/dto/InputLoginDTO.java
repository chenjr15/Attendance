package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InputLoginDTO {
    @Schema(required = true, description = "账号,用户名、邮箱、手机号等", example = "user1")
    private String account;
    @Schema(required = true, description = "密码", example = "my_password")
    private String password;
}
