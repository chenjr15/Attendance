package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sun.istack.NotNull;
import dev.chenjr.attendance.service.dto.validation.KeyWord;
import dev.chenjr.attendance.service.dto.validation.KeyWordValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class OrganizationDTO {
  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(type = "string", description = "组织结构id")
  private Long id;

  @NotNull
  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(type = "string", description = "父级id，如果为0则为顶级结构", example = "0")
  private Long parentId;

  @NotBlank
  @Schema(description = "组织结构名称", example = "某某大学")
  private String name;

  @Length(max = 2048)
  @Schema(description = "关于该结构的附加说明")
  private String comment;

  @KeyWord
  @NotBlank
  @Schema(description = "类型", example = "院校", pattern = KeyWordValidator.RE_KEYWORD)
  private String orgType;

  @Schema(description = "所属省份，", example = "北京")
  private String province;

  @Schema(description = "所属省份id", example = "11")
  private Long provinceId;

  @Schema(description = "所有的祖先节点", example = "xx大学-xx学院-xx专业")
  private String parents;

  @Schema(description = "子结构数量")
  private int childrenCount;
  @Schema(description = "子结构")
  private List<OrganizationDTO> children;
}
