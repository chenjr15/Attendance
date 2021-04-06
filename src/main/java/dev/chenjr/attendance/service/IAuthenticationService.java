package dev.chenjr.attendance.service;

import dev.chenjr.attendance.entity.AccountInfo;
import dev.chenjr.attendance.entity.User;
import dev.chenjr.attendance.service.dto.LoginRequest;


public interface IAuthenticationService extends IService {
    boolean checkPasswordAndAccount(String account, String rawPassword);

    void setUserRole(User user, String role);

    void unsetUserRole(User user, String role);

    void changePassword(long uid, String password);

    AccountInfo getAuth(long uid);

    boolean setAuth(long uid, String password);

    String createLoginToken(LoginRequest loginRequest);
}
