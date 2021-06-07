package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.dao.enums.ParamEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysParameterDTO {

    @Schema(description = "参数英文标识,该标识不可修改", example = "course_sign_exp")
    private String code;

    @NotBlank
    @Schema(description = "参数名", example = "签到经验值")
    private String name;


    @NotBlank
    @Schema(description = "参数的值,比如签到一次 +10经验值", example = "10")
    private String value;

    @SuppressWarnings("FieldMayBeFinal")
    @Schema(description = "参数类型", example = "0")
    private Integer paramType = ParamEnum.DOUBLE.getValue();

    @Schema(description = "该项的描述", example = "该参数设置每次签到的经验值")
    private String description;

    @Schema(description = "参数可选值,逗号分割。字符串型就是可选值，数值型就是范围", defaultValue = "10,5000")
    private String range;
}
