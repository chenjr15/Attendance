package dev.chenjr.attendance.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {
    public final static int UNKNOWN_GENDER = 0;
    public final static int MALE = 1;
    public final static int FEMALE = 2;

    private String realName;
    private String loginName;
    private String email;
    private String phone;
    private int gender;
    private String roles;

}
