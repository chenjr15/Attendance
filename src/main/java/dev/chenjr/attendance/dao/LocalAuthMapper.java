package dev.chenjr.attendance.dao;

import dev.chenjr.attendance.dao.entity.LocalAuth;
import org.apache.ibatis.annotations.*;


import java.util.List;
//@Mapper
public interface LocalAuthMapper {
    @Select("SELECT * FROM LocalAuth WHERE id = #{id}")
    LocalAuth getById(@Param("id") long id);

    @Select("SELECT * FROM LocalAuth WHERE uid = #{uid}")
    LocalAuth getByUid(@Param("uid") long uid);

    @Select("SELECT * FROM LocalAuth LIMIT #{offset}, #{maxResults}")
    List<LocalAuth> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO LocalAuth (uid,password,salt) VALUES (#{auth.uid}, #{auth.password}, #{auth.salt})")
    void insert(@Param("auth") LocalAuth auth);

    /**
     *  只更新密码盐以及更新时间, 根据uid更改
     * @param auth 账号密码盐
     */
    @Update("UPDATE LocalAuth SET password=#{auth.password},salt=#{auth.salt} WHERE uid = #{auth.uid}")
    void update(@Param("auth") LocalAuth auth);

    @Delete("DELETE FROM LocalAuth WHERE id = #{id}")
    void deleteById(@Param("id") long id);
    @Delete("DELETE FROM LocalAuth WHERE uid = #{uid}")
    void deleteByUid(@Param("uid") long uid);
}
