package dev.chenjr.attendance.dao.entity;


public class User extends BaseEntity {
    public final static int UNKNOWN_GENDER = 0;
    public final static int MALE = 1;
    public final static int FEMALE = 2;

    private String name;
    private String email;
    private String phone;
    private int gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
