package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 菜单项表，自己和自己关联形成的多级菜单
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单项表，自己和自己关联形成的多级菜单")
public class Menu extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    private Long parentId;
    
    private String name;
    
    private String path;
    
    private Integer type;
    
    private String icon;
    
    private Integer orderValue;
    
    public Menu(long id, int orderValue) {
        super();
        this.setId(id);
        this.orderValue = orderValue;
    }
    
}
