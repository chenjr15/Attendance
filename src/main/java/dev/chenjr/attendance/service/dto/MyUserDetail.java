package dev.chenjr.attendance.service.dto;

import dev.chenjr.attendance.dao.old.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * 用来给Spring Security 用的UserDetail
 * TODO 待重构
 */
public class MyUserDetail extends org.springframework.security.core.userdetails.User {


    private String password;

    public MyUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

    }


    public MyUserDetail(User user) {
        this(user.getLoginName(), "", AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
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
}
