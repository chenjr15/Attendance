package dev.chenjr.attendance.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocalAuth extends BaseEntity {
    private long uid;

    private String password;
    private String salt;

    public LocalAuth(long uid) {
        this.uid = uid;
    }

    public LocalAuth(long uid, String password, String salt) {
        this.uid = uid;
        this.password = password;
        this.salt = salt;
    }

    public LocalAuth() {

    }
}
