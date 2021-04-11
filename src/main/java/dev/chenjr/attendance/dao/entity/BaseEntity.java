package dev.chenjr.attendance.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity 实体类的基类，包含id、创建时间之类的属性
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2522651338542656734L;
    private Long id;
    private Long creator;
    private Long updater;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public BaseEntity() {
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

}
