package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputSysParameterDTO {

    @Schema(description = "参数名", example = "签到经验值")
    private String name;

    @Schema(description = "参数英文标识", example = "class_course_sign_exp")
    private String code;

    @Schema(description = "该参数的值", example = "10")
    private String value;

    @Schema(description = "该参数的类型", example = "Integer")
    private String type;

    @Schema(description = "该项的描述")
    private String description;
}
