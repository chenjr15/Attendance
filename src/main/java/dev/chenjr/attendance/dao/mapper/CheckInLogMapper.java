package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.CheckInLog;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * <p>
 * 签到记录表，记录用户在每门课的签到时间签到地点，对应的签到任务id Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface CheckInLogMapper extends MyBaseMapper<CheckInLog> {

    /**
     * @param id 指定的主键
     * @return !不存在返回 null, 存在返回true,
     */
    @Override
    @Select("SELECT 1 FROM check_in_log WHERE id=#{id} limit 1 ")
    Optional<Boolean> exists(long id);

    /**
     * 根据任务id 和学生id 查找签到记录
     *
     * @param taskId 任务id
     * @param uid    学生id
     * @return 签到记录
     */
    @Select("SELECT * FROM check_in_log WHERE task_id=#{taskId} and uid=#{uid} limit 1 ")
    CheckInLog selectByTaskAndStu(long taskId, long uid);

    @Select("SELECT 1 FROM check_in_log WHERE task_id=#{taskId} and uid=#{uid} limit 1")
    Boolean checked(long uid, long taskId);
}
