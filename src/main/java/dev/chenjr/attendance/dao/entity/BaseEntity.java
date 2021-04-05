package dev.chenjr.attendance.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public abstract class BaseEntity implements Serializable {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;

//    public ZonedDateTime getUpdatedDateTime() {
//        return updatedAt.toInstant().atZone(ZoneId.systemDefault());
//    }
//
//    public ZonedDateTime getCreatedDateTime() {
//        return this.createdAt.toInstant().atZone(ZoneId.systemDefault());
//    }


}
