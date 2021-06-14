package dev.chenjr.attendance.dao.entity;

import dev.chenjr.attendance.dao.enums.ParamEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统参数，一张表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemParam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "参数中文名", example = "签到范围(米)")
    private String paramName;

    @Schema(description = "参数标识，标识不可修改", example = "max_check_in_distance")
    private String paramCode;

    @SuppressWarnings("FieldMayBeFinal")
    @Schema(description = "参数类型", example = "0")
    private Integer paramType = ParamEnum.DOUBLE.getValue();

    @Schema(description = "参数值", example = "100")
    private String value;

    @Schema(description = "参数可选值,逗号分割。字符串型就是可选值，数值型就是范围", defaultValue = "10,5000")
    private String paramRange;

    @Schema(description = "参数的描述", defaultValue = "该项参数设定签到的有效距离，超过该距离就算范围外了")
    private String description;
}
