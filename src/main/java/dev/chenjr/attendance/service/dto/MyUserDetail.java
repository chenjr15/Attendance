package dev.chenjr.attendance.service.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.chenjr.attendance.dao.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 用来给Spring Security 用的UserDetail
 * TODO 待重构
 */
public class MyUserDetail extends org.springframework.security.core.userdetails.User {


    private String password;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    public MyUserDetail(String account, String password, long uid, Collection<? extends GrantedAuthority> authorities) {
        super(account, password, authorities);
        this.uid = uid;

    }


    public MyUserDetail(User user, String password, Collection<? extends GrantedAuthority> authorities) {
        this(user.getLoginName(), password, user.getId(), authorities);
    }

    @Override
    public String getPassword() {
        if (this.password == null || this.password.length() == 0) {
            return super.getPassword();
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUid() {
        return uid;
    }
}
