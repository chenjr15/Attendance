package dev.chenjr.attendance.service.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.chenjr.attendance.service.dto.validation.LoginName;
import dev.chenjr.attendance.service.dto.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type = "string", description = "userid会返回字符串格式的uid以防止json数字精度丢失问题")
    private Long id;

    @LoginName
    private String loginName;

    private String realName;

    private String gender;

    @Email
    private String email;

    @PhoneNumber
    private String phone;

    @Schema(description = "学工号")
    private String academicId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "学校院系专业ID", example = "102210")
    private Long schoolMajorID;
    @Schema(description = "学校院系专业名", example = "xx大学-计算机学院-软件工程专业-软工1班")
    private String schoolMajorName;

    @Schema(description = "头像链接")
    private String avatar;
}
