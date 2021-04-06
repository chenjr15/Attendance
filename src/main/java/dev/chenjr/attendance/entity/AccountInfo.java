package dev.chenjr.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo extends BaseEntity {
    private long uid;

    private String password;
    private boolean locked;

    private boolean expired;

    public AccountInfo(long uid) {
        this.uid = uid;
    }

    public AccountInfo(long uid, String password) {
        this.uid = uid;
        this.password = password;

    }

}
