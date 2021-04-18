package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * Account 表 指定登陆方式，如用户名，邮箱，手机号，其他第三方登陆方式等
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Account对象", description = "Account 表 指定登陆方式，如用户名，邮箱，手机号，其他第三方登陆方式等")
public class Account extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String type;

    private String account;
    @Schema(description = "对于本地账号是密码")
    private String token;

    private Boolean locked;

    private LocalDateTime lastLoginDate;

    @Schema(description = "是否内部登陆方式(手机号、用户名、邮箱)")
    private Boolean internal;

    public static Account fromLoginName(long userId, String loginName, String password) {
        return new Account(userId, TYPE_LOGIN_NAME, loginName, password, false, LocalDateTime.now(), true);
    }

    public static Account fromPhone(long userId, String phone, String password) {
        return new Account(userId, TYPE_PHONE, phone, password, false, LocalDateTime.now(), true);
    }

    public static Account fromEmail(long userId, String email, String password) {
        return new Account(userId, TYPE_EMAIL, email, password, false, LocalDateTime.now(), true);
    }

    public static final String TYPE_LOGIN_NAME = "login_name";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_EMAIL = "email";

}
