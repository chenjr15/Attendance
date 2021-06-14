package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "字典详情DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryDetailDTO {

    @Schema(description = "明细项id")
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    @Schema(description = "明细项数据库代码", example = "1")
    Integer value;
    @Schema(description = "明细项前端显示名字", example = "男")
    String name;
    @Schema(description = "明细项英文名字", example = "male")
    String code;
    @Schema(description = "是否默认", example = "true")
    Boolean isDefault;
    @Schema(description = "显示顺序", example = "0")
    Integer order;
    @Schema(description = "是否显示", example = "true")
    Boolean shouldDisplay;

}
