package dev.chenjr.attendance.service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String loginName;

    private String realName;

    private String gender;

    private String email;

    private String phone;

    @Schema(description = "学工号")
    private String academicId;

    @Schema(description = "学校院系专业ID", example = "102210")
    private Long schoolMajorID;
    @Schema(description = "学校院系专业名", example = "xx大学-计算机学院-软件工程专业-软工1班")
    private String schoolMajorName;
}
