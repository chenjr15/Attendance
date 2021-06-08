package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DictionaryDTO {
    @Schema(description = "字典项名", example = "性别")
    private String name;

    @Schema(description = "字典项英文标识", example = "dict_gender")
    private String code;

    @Schema(description = "默认的项的Item")
    private String defaultCode;
    
    @Schema(description = "包含的子项")
    private List<DictionaryDetailDTO> details;

}
