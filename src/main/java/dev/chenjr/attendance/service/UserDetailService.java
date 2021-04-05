package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.AccountInfo;
import dev.chenjr.attendance.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 为了满足同时包含两个服务弄的类
 */
@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 开始查找用户
        User user = userService.getUserByLoginName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Can not find the given username.");
        }
        AccountInfo auth = authenticationService.getAuth(user.getId());
        if (auth == null) {
            throw new UsernameNotFoundException("Password may be unset.");
        }


        MyUserDetail userDetail = new MyUserDetail(user);
        userDetail.setPassword(auth.getPassword());
        return userDetail;
    }
}
