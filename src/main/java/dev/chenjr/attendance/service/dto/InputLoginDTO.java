package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class InputLoginDTO {
    @Length(min = 6, max = 20)
    @Schema(required = true, description = "账号,用户名、邮箱、手机号等", example = "user1")
    private String account;
    @Length(min = 5, max = 20)
    @Schema(required = true, description = "密码", example = "my_password")
    private String password;
}
