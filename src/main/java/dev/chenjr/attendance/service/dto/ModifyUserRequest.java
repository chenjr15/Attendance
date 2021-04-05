package dev.chenjr.attendance.service.dto;

import lombok.Data;

@Data
public class ModifyUserRequest {
    private String name;
    private String email;
    private String phone;
    private Integer gender;
    private String roles;
    private String password;
}
