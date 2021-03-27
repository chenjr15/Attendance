package dev.chenjr.attendance.dao;

import dev.chenjr.attendance.dao.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//@Component
public interface UserMapper {

    @Select("SELECT * FROM User WHERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM User WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM User WHERE phone = #{phone}")
    User getByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM User LIMIT #{offset}, #{maxResults}")
    List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO User (name, phone,gender,createdAt) VALUES (#{user.name}, #{user.phone}, #{user.gender})")
    void insert(@Param("user") User user);

    @Update("UPDATE User SET name = #{user.name},phone=#{user.phone},gender=#{user.gender} WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM User WHERE id = #{id}")
    void deleteById(@Param("id") long id);

    // 判断是否存在，注意这里不能用boolean, 必须要用包装类, 找不到的时候会返回null, 因此需要判断是否为空
    // 优化：select 1 意味着存在就会返回1, limit 1 表示只要查一行即可(虽然也最多只有一行)
    @Select("SELECT 1 FROM User WHERE id = #{id} limit 1 ")
    Boolean idExists(@Param("id") long id);

    @Select("SELECT 1 FROM User WHERE phone = #{phone} limit 1 ")
    Boolean phoneExists(@Param("phone") String phone);

    @Select("SELECT 1 FROM User WHERE email = #{email} limit 1 ")
    Boolean emailExists(@Param("email") String email);
}
