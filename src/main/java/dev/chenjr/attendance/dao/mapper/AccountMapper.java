package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Account 表 指定登陆方式，如用户名，邮箱，手机号，其他第三方登陆方式等 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface AccountMapper extends MyBaseMapper<Account> {
    @Select("SELECT * FROM Account WHERE user_id = #{uid}")
    Account getByUid(@Param("uid") long uid);

    @Delete("DELETE FROM Account WHERE user_id = #{uid}")
    void deleteByUid(@Param("uid") long uid);

    @Delete("SELECT * FROM Account WHERE account = #{account}")
    Account getByAccount(@Param("account") String account);

    @Select("SELECT 1 FROM user account=#{account} limit 1 ")
    Boolean existsByAccount(@Param("account") String account);
}
