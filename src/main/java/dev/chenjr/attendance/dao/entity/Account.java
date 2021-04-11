package dev.chenjr.attendance.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Schema(name="Account对象", description="Account 表 指定登陆方式，如用户名，邮箱，手机号，其他第三方登陆方式等")
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


}
