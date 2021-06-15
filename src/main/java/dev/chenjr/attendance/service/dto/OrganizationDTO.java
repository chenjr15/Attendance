package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long id;

  @NotNull
  @Schema(type = "string", description = "父级id，如果为0则为顶级结构", example = "0")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonSerialize(using = ToStringSerializer.class)
  private Long parentId;

  @NotBlank
  @Schema(description = "组织结构名称", example = "某某大学")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String name;

  @Length(max = 2048)
  @Schema(description = "关于该结构的附加说明")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String comment;

  @KeyWord
  @NotBlank
  @Schema(description = "类型", example = "院校", pattern = KeyWordValidator.RE_KEYWORD)
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String orgType;

  @Schema(description = "所属省份，", example = "北京")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String province;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "所属省份id", example = "11")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long provinceId;

  @Schema(description = "所有的祖先节点", example = "xx大学-xx学院-xx专业")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String parents;

  @Schema(description = "子结构数量")
  private Integer childrenCount;

  @Schema(description = "子结构")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<OrganizationDTO> children;
}
