package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(type = "string", description = "组织结构id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long id;
  
  @NotNull
  @Schema(type = "string", description = "父级id，如果为0则为顶级结构", example = "0")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonSerialize(using = ToStringSerializer.class)
  private Long parentId;
  
  @Schema(description = "显示名字")
  private String title;
  
  @Schema(description = "路由")
  private String index;
  
  @Schema(description = "类型")
  private Integer type;
  
  @Schema(description = "图标")
  private String icon;
  
  @Schema(description = "排序")
  private Integer orderValue;
  
  @Schema(description = "子菜单数量")
  public long childrenCount;
  
  @Schema(description = "子菜单")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<MenuDTO> subs;
}
