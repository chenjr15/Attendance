package dev.chenjr.attendance.dao.mapper;

import dev.chenjr.attendance.dao.entity.SignupLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 签到记录表，记录用户在每门课的签到时间签到地点，对应的签到任务id Mapper 接口
 * </p>
 *
 * @author chenjr
 * @since 2021-04-11
 */
public interface SignupLogMapper extends BaseMapper<SignupLog> {

}
