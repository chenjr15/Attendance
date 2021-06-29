package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type = "string")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    
    private String name;
    
    private String type;
    
}
