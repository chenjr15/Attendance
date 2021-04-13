package dev.chenjr.attendance.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@Schema(description = "数据字典类别对象")
@TableName("Dictionary")
public class Dictionary extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "字典项名，如性别，具体的选项在detail表里")
    private String name;

    @Schema(description = "该项的描述")
    private String description;


}
