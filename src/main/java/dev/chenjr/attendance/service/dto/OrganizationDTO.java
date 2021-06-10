package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationDTO {
  @Schema(description = "组织结构id")
  private Long id;
  @Schema(description = "父级id，如果为0则为顶级结构", example = "0")
  private Long parentId;
  @Schema(description = "组织结构名称", example = "某某大学")
  private String name;
  @Schema(description = "关于该结构的附加说明")
  private String comment;
  @Schema(description = "类型", example = "院校")
  private String orgType;
  @Schema(description = "所属省份，，0为其他", example = "北京")
  private String province;
  @Schema(description = "子结构数量")
  private Long childrenCount;
  @Schema(description = "子结构")
  private List<OrganizationDTO> children;
}
