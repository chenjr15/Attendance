package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.CheckInTask;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface CheckInTaskMapper extends MyBaseMapper<CheckInTask> {
    /**
     * @param id 指定的主键
     * @return !不存在返回 null, 存在返回true,
     */
    @Override
    @Select("SELECT 1 FROM check_in_task WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(long id);

    @Select("SELECT * FROM check_in_task " +
            "WHERE course_id = #{cid} " +
            "AND  ( deadline=NULL OR  deadline >= #{dateNow}) " +
            "LIMIT 1")
    CheckInTask current(long cid, LocalDateTime dateNow);
}
