package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据字典中的每个类别的具体项")
public class DictionaryDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long dictionaryId;

    @Schema(description = "存在数据库里的项id，如1,2,3")
    private Integer itemKey;

    @Schema(description = "显示在前端的文本, 如男，女，未知")
    private String itemValue;

    private Integer orderValue;

    @Schema(description = "该项是否默认")
    private Boolean defaultItem;

    private Integer display;

    private String permission;


}
