package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DictionaryDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "数据字典id")
    Long id;
    
    @Schema(description = "字典项名", example = "性别")
    private String name;

    @Schema(description = "字典项英文标识", example = "dict_gender")
    private String code;

    @Schema(description = "默认的项的Item")
    private String defaultCode;
    @Schema(description = "该项的描述")
    private String description;
    @Schema(description = "包含的子项")
    private List<DictionaryDetailDTO> details;

}
