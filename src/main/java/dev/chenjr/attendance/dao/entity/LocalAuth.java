package dev.chenjr.attendance.dao.entity;

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


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
