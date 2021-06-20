package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.service.ICheckInService;
import dev.chenjr.attendance.service.dto.CheckInTaskDTO;
import dev.chenjr.attendance.service.dto.CheckInTaskLogDTO;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import org.springframework.stereotype.Service;

@Service
public class CheckInService implements ICheckInService {
    /**
     * @param pageSort
     * @return 所有的签到任务(分页后)
     */
    @Override
    public PageWrapper<CheckInTaskDTO> listAllTasks(PageSort pageSort) {
        return null;
    }

    /**
     * 返回某个课程的签到任务
     *
     * @param courseId 课程id
     * @param pageSort 分页参数
     * @return 该课程的签到任务
     */
    @Override
    public PageWrapper<CheckInTaskDTO> listCourseTasks(long courseId, PageSort pageSort) {
        return null;
    }

    /**
     * 获取某次签到的详细信息
     *
     * @param taskId 任务id
     * @return 详细信息
     */
    @Override
    public CheckInTaskDTO getTask(long taskId) {
        return null;
    }

    /**
     * 修改签到任务
     *
     * @param checkInTaskDTO 要修改的属性
     * @return 修改后的数据
     */
    @Override
    public CheckInTaskDTO modifyTask(CheckInTaskDTO checkInTaskDTO) {
        return null;
    }

    /**
     * 获取签到记录
     *
     * @param taskId 签到任务id
     * @return 签到记录
     */
    @Override
    public PageWrapper<CheckInTaskLogDTO> listCheckInLogs(long taskId) {
        return null;
    }

    /**
     * 删除签到任务
     *
     * @param taskId 签到任务id
     */
    @Override
    public void deleteTask(long taskId) {

    }

    /**
     * 修改签到任务
     *
     * @param logDTO 要修改的任务
     * @return 修改后的结果
     */
    @Override
    public CheckInTaskLogDTO modifyLog(CheckInTaskLogDTO logDTO) {
        return null;
    }

    /**
     * 签到
     *
     * @param taskId 任务id
     * @param logDTO 签到记录信息
     * @return 创建的签到数据
     */
    @Override
    public CheckInTaskLogDTO checkIn(Long taskId, CheckInTaskLogDTO logDTO) {
        return null;
    }
}
