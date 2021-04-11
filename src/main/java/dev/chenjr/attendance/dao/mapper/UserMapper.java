package dev.chenjr.attendance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.chenjr.attendance.dao.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表，所有系统内的用户都在这个表里 Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User getByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM user WHERE login_name = #{loginName}")
    User getByLoginName(@Param("loginName") String loginName);

    // 判断是否存在，注意这里不能用boolean, 必须要用包装类, 找不到的时候会返回null, 因此需要判断是否为空
    // 优化：select 1 意味着存在就会返回1, limit 1 表示只要查一行即可(虽然也最多只有一行)
    @Select("SELECT 1 FROM user WHERE id = #{id} limit 1 ")
    Boolean idExists(@Param("id") long id);

    @Select("SELECT 1 FROM user WHERE phone = #{phone} limit 1 ")
    Boolean phoneExists(@Param("phone") String phone);

    @Select("SELECT 1 FROM user WHERE email = #{email} limit 1 ")
    Boolean emailExists(@Param("email") String email);

    @Select("SELECT 1 FROM user WHERE login_name = #{loginName} limit 1 ")
    Boolean loginNameExists(@Param("loginName") String loginName);
}
