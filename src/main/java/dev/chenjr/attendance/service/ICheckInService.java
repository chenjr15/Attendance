package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.CheckInLogDTO;
import dev.chenjr.attendance.service.dto.CheckInTaskDTO;
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
     * @param updater        更新人
     * @param checkInTaskDTO 要修改的属性
     * @return 修改后的数据
     */
    CheckInTaskDTO modifyTask(User updater, CheckInTaskDTO checkInTaskDTO);

    /**
     * 获取签到记录
     *
     * @param taskId   签到任务id
     * @param pageSort 排序
     * @return 签到记录
     */
    PageWrapper<CheckInLogDTO> listCheckInLogs(long taskId, PageSort pageSort);

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
    CheckInLogDTO modifyLog(CheckInLogDTO logDTO);

    /**
     * 签到
     *
     * @param operator 操作人
     * @param taskId   任务id
     * @param logDTO   签到记录信息
     * @return 创建的签到数据
     */
    CheckInLogDTO checkIn(User operator, Long taskId, CheckInLogDTO logDTO);

    /**
     * 创建签到任务/发起签到
     *
     * @param checkInTaskDTO 签到信息
     * @return 签到成功的信息
     */
    CheckInTaskDTO createCheckInTask(CheckInTaskDTO checkInTaskDTO);

    /**
     * 结束签到
     *
     * @param operator 操作人
     * @param taskId   任务id
     */
    void endCheckInTask(User operator, long taskId);

    /**
     * 修改某个任务下某个学生的签到情况
     *
     * @param modifier 修改人
     * @param taskId   签到任务id
     * @param stuId    学生id
     * @param status   签到状态
     * @return 修改后的记录dto
     */
    CheckInLogDTO modifyCheckInStatus(User modifier, long taskId, long stuId, int status);

    /**
     * 获取某个课程当前的签到任务，如果没有签到任务会返回404
     *
     * @param courseId 课程id
     * @return 当前的签到任务
     */
    CheckInTaskDTO getCurrentCheckInTask(long courseId);
}
