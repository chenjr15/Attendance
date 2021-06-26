package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type = "string", description = "组织结构id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    
    @Schema(description = "中文名")
    private String name;
    @Schema(description = "英文名")
    private String code;
}
