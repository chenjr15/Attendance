package dev.chenjr.attendance.dao.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity 实体类的基类，包含id、创建时间之类的属性
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 2522651338542656734L;
    // JSON 格式不支持太大的数字，需要用字符串处理
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long creator;
    private Long updater;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public BaseEntity() {
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    public void createBy(Long uid) {
        this.createTime = LocalDateTime.now();
        this.creator = uid;
        this.updateTime = LocalDateTime.now();
        this.updater = uid;
    }

    public void updateBy(Long uid) {
        this.updateTime = LocalDateTime.now();
        this.updater = uid;
    }

}
