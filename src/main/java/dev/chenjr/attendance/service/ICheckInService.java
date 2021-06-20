package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.CheckInTaskDTO;
import dev.chenjr.attendance.service.dto.CheckInTaskLogDTO;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;

public interface ICheckInService {
    /**
     * @return 所有的签到任务(分页后)
     */
    PageWrapper<CheckInTaskDTO> listAllTasks(PageSort pageSort);

    /**
     * 返回某个课程的签到任务
     *
     * @param courseId 课程id
     * @param pageSort 分页参数
     * @return 该课程的签到任务
     */
    PageWrapper<CheckInTaskDTO> listCourseTasks(long courseId, PageSort pageSort);

    /**
     * 获取某次签到的详细信息
     *
     * @param taskId 任务id
     * @return 详细信息
     */
    CheckInTaskDTO getTask(long taskId);

    /**
     * 修改签到任务
     *
     * @param checkInTaskDTO 要修改的属性
     * @return 修改后的数据
     */
    CheckInTaskDTO modifyTask(CheckInTaskDTO checkInTaskDTO);

    /**
     * 获取签到记录
     *
     * @param taskId 签到任务id
     * @return 签到记录
     */
    PageWrapper<CheckInTaskLogDTO> listCheckInLogs(long taskId);

    /**
     * 删除签到任务
     *
     * @param taskId 签到任务id
     */
    void deleteTask(long taskId);

    /**
     * 修改签到任务
     *
     * @param logDTO 要修改的任务
     * @return 修改后的结果
     */
    CheckInTaskLogDTO modifyLog(CheckInTaskLogDTO logDTO);

    /**
     * 签到
     *
     * @param taskId 任务id
     * @param logDTO 签到记录信息
     * @return 创建的签到数据
     */
    CheckInTaskLogDTO checkIn(Long taskId, CheckInTaskLogDTO logDTO);
}
