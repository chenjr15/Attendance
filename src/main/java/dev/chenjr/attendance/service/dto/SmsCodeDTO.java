package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.service.dto.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class SmsCodeDTO {
    @NotBlank
    @Schema(example = "register,login,reset_password")
    public String type;
    @NotBlank
    @PhoneNumber
    public String phone;
}
