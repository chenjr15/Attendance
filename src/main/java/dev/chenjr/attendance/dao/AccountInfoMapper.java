package dev.chenjr.attendance.dao;

import dev.chenjr.attendance.dao.entity.AccountInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Mapper
public interface AccountInfoMapper {
    @Select("SELECT * FROM LocalAuth WHERE id = #{id}")
    AccountInfo getById(@Param("id") long id);

    @Select("SELECT * FROM LocalAuth WHERE uid = #{uid}")
    AccountInfo getByUid(@Param("uid") long uid);

    @Select("SELECT * FROM LocalAuth LIMIT #{offset}, #{maxResults}")
    List<AccountInfo> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO LocalAuth (uid,password) VALUES (#{auth.uid}, #{auth.password})")
    void insert(@Param("auth") AccountInfo auth);

    /**
     * 只更新密码盐以及更新时间, 根据uid更改
     *
     * @param auth 账号密码盐
     */
    @Update("UPDATE LocalAuth SET password=#{auth.password} WHERE uid = #{auth.uid}")
    void update(@Param("auth") AccountInfo auth);

    @Delete("DELETE FROM LocalAuth WHERE id = #{id}")
    void deleteById(@Param("id") long id);
    @Delete("DELETE FROM LocalAuth WHERE uid = #{uid}")
    void deleteByUid(@Param("uid") long uid);
}
