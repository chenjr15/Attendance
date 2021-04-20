package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class SmsCodeDTO {
    @NotBlank
    @Schema(example = "register,login,reset_password")
    public String type;
    @NotBlank
    public String phone;
}
