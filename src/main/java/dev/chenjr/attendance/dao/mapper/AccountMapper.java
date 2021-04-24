package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Account 表 指定登陆方式，如用户名，邮箱，手机号，其他第三方登陆方式等 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface AccountMapper extends MyBaseMapper<Account> {
    @Select("SELECT * FROM account WHERE user_id = #{uid} LIMIT 1")
    Account getByUid(@Param("uid") long uid);

    @Select("SELECT * FROM account WHERE user_id = #{uid} ")
    List<Account> getAllByUid(@Param("uid") long uid);

    @Delete("DELETE FROM account WHERE user_id = #{uid}")
    void deleteByUid(@Param("uid") long uid);

//    @Select("SELECT * FROM account WHERE account = #{account}")
//    List<Account> getAllByAccount(@Param("account") String account);

    @Select("SELECT * FROM account WHERE account = #{account} LIMIT 1")
    Account getOneByAccount(@Param("account") String account);

    @Select("SELECT 1 FROM account WHERE account=#{account} LIMIT 1 ")
    Boolean existsByAccount(@Param("account") String account);
}
