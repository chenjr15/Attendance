package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.LocalAuthMapper;
import dev.chenjr.attendance.dao.entity.LocalAuth;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService extends BaseService {
    @Autowired
    UserService userService;
    @Autowired
    LocalAuthMapper localAuthMapper;

    /**
     * 检验账号和密码是否匹配
     *
     * @param account  邮箱或者手机号
     * @param password 密码
     * @return 是否匹配
     */
    public Boolean checkPasswordAndAccount(String account, String password) {
        User user = userService.getUserByAccount(account);
        if (user == null) {
            return false;
        }
        LocalAuth localAuth;
        localAuth = localAuthMapper.getByUid(user.getId());
        if (localAuth == null) {
            return false;
        }
        String correctHash = localAuth.getPassword();
        String encodeHash = this.encodeHash(password, localAuth.getSalt());
        return correctHash.equals(encodeHash);


    }

    private String encodeHash(String password, String salt) {

        return password + salt;
    }

    public void changePassword(long uid, String password) {
        this.setAuth(uid,password);
    }

    public LocalAuth getAuth(long uid) {
        return this.localAuthMapper.getByUid(uid);
    }


    public boolean setAuth(long uid, String password) {
        boolean exists = userService.userExists(uid);
        if (!exists) {
            return false;
        }
        String salt = RandomUtil.randomString(12);
        String passwordHash = encodeHash(password, salt);
        LocalAuth localAuth = this.getAuth(uid);
        if (localAuth == null) {
            // 新建密码登陆信息
            localAuth = new LocalAuth(uid, passwordHash, salt);
            localAuthMapper.insert(localAuth);
        } else {
            localAuth.setPassword(passwordHash);
            localAuth.setSalt(salt);
            localAuthMapper.update(localAuth);
        }
        return true;
    }
}
