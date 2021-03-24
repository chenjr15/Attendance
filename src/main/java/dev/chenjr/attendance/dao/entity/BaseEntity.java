package dev.chenjr.attendance.dao.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class BaseEntity {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedAt.toInstant().atZone(ZoneId.systemDefault());
    }

    public ZonedDateTime getCreatedDateTime() {
        return this.createdAt.toInstant().atZone(ZoneId.systemDefault());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
