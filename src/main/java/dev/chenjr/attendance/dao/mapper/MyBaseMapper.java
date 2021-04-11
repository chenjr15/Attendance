package dev.chenjr.attendance.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 判断指定id的记录是否存在
     * 注意这里不能用boolean, 必须要用包装类, 找不到的时候会返回null, 因此需要判断是否为空
     * 优化：select 1 意味着存在就会返回1, limit 1 表示只要查一行即可(虽然也最多只有一行)
     *
     * @param id 指定的主键
     * @return !不存在返回 null, 存在返回true,
     */
    @Select("SELECT 1 FROM user id=#{id} limit 1 ")
    Boolean exists(@Param("id") long id);
}
