package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.CheckInLog;
import dev.chenjr.attendance.dao.entity.CheckInTask;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.CheckInLogMapper;
import dev.chenjr.attendance.dao.mapper.CheckInTaskMapper;
import dev.chenjr.attendance.dao.mapper.CourseMapper;
import dev.chenjr.attendance.dao.mapper.UserCourseMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.ICheckInService;
import dev.chenjr.attendance.service.ISysParamService;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.chenjr.attendance.dao.entity.CheckInLog.*;
import static java.lang.Math.toRadians;

@Slf4j
@Service
public class CheckInService implements ICheckInService {
    @Autowired
    CheckInTaskMapper taskMapper;
    @Autowired
    CheckInLogMapper logMapper;
    @Autowired
    IUserService userService;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    UserCourseMapper userCourseMapper;
    
    @Autowired
    ISysParamService sysParamService;
    
    /**
     * @param pageSort 分页排序筛选
     * @return 所有的签到任务(分页后)
     */
    @Override
    public PageWrapper<CheckInTaskDTO> listAllTasks(PageSort pageSort) {
        QueryWrapper<CheckInTask> qw = new QueryWrapper<>();
        Page<CheckInTask> page = pageSort.getPage();
        qw = pageSort.buildQueryWrapper(qw, "description");
        page = taskMapper.selectPage(page, qw);
        List<CheckInTaskDTO> collected = page.getRecords().stream().map(this::task2dto).collect(Collectors.toList());
        return PageWrapper.fromList(page, collected);
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
        
        QueryWrapper<CheckInTask> qw = new QueryWrapper<>();
        qw = qw.eq("course_id", courseId);
        Page<CheckInTask> page = pageSort.getPage();
        qw = pageSort.buildQueryWrapper(qw, "description");
        page = taskMapper.selectPage(page, qw);
        List<CheckInTaskDTO> collected = page.getRecords().stream().map(this::task2dto).collect(Collectors.toList());
        return PageWrapper.fromList(page, collected);
    }
    
    /**
     * 获取某次签到的详细信息
     *
     * @param taskId 任务id
     * @return 详细信息
     */
    @Override
    public CheckInTaskDTO getTask(long taskId) {
        CheckInTask checkInTask = taskMapper.selectById(taskId);
        if (checkInTask == null) {
            throw HttpStatusException.notFound();
        }
        return task2dto(checkInTask);
    }
    
    /**
     * 修改签到任务
     *
     * @param updater        更新人
     * @param checkInTaskDTO 要修改的属性
     * @return 修改后的数据
     */
    @Override
    public CheckInTaskDTO modifyTask(User updater, CheckInTaskDTO checkInTaskDTO) {
        Optional<Boolean> exists = taskMapper.exists(checkInTaskDTO.getId());
        if (!exists.orElse(false)) {
            throw HttpStatusException.notFound();
        }
        
        CheckInTask checkInTask = dto2task(checkInTaskDTO);
        checkInTask.updateBy(updater.getId());
        taskMapper.updateById(checkInTask);
        return this.getTask(checkInTask.getId());
    }
    
    /**
     * 获取签到记录
     *
     * @param taskId   签到任务id
     * @param pageSort 排序
     * @return 签到记录
     */
    @Override
    public PageWrapper<CheckInResultDTO> listCheckInLogs(long taskId, PageSort pageSort) {
        Page<CheckInLog> page = pageSort.getPage();
        QueryWrapper<CheckInLog> qw = new QueryWrapper<>();
        qw = qw.eq("task_id", taskId);
        qw = pageSort.buildQueryWrapper(qw);
        page = logMapper.selectPage(page, qw);
        List<CheckInResultDTO> collect = page.getRecords().stream().map(this::log2result).collect(Collectors.toList());
        return PageWrapper.fromList(page, collect);
    }
    
    
    /**
     * 删除签到任务
     *
     * @param taskId 签到任务id
     */
    @Override
    public void deleteTask(long taskId) {
        if (!taskMapper.exists(taskId).orElse(false)) {
            throw HttpStatusException.notFound();
        }
        taskMapper.deleteById(taskId);
    }
    
    /**
     * 修改签到任务
     *
     * @param logDTO 要修改的任务
     * @return 修改后的结果
     */
    @Override
    public CheckInLogDTO modifyLog(CheckInLogDTO logDTO) {
        Optional<Boolean> exists = logMapper.exists(logDTO.getId());
        if (!exists.orElse(false)) {
            throw HttpStatusException.notFound();
        }
        CheckInLog checkInLog = dto2log(logDTO);
        logMapper.updateById(checkInLog);
        return this.getLog(checkInLog.getId());
    }
    
    /**
     * 获取某次签到记录的详细信息
     *
     * @param logId 记录id
     * @return 详细信息
     */
    
    public CheckInLogDTO getLog(long logId) {
        CheckInLog checkInLog = logMapper.selectById(logId);
        if (checkInLog == null) {
            throw HttpStatusException.notFound();
        }
        return log2dto(checkInLog);
    }
    
    /**
     * 签到
     *
     * @param operator 操作人
     * @param taskId   任务id
     * @param logDTO   签到记录信息
     * @return 创建的签到数据
     */
    @Override
    public CheckInLogDTO checkIn(User operator, Long taskId, CheckInLogDTO logDTO) {
        /* 1. 检查签到任务是否存在 */
        CheckInTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw HttpStatusException.notFound("签到任务不存在!");
        }
        /* 2. 检查签到是否结束 */
        if (task.isFinished()) {
            throw HttpStatusException.badRequest("签到失败: 签到已结束！");
        }
        /* 3. 检查是否选课 */
        Boolean elected = userCourseMapper.isElected(logDTO.getUid(), task.getCourseId());
        if (elected == null) {
            throw HttpStatusException.badRequest("未加入该班课！");
        }
        /* 4. 检查是否已经签到 */
        Boolean checked = logMapper.isChecked(logDTO.getUid(), task.getId());
        if (checked != null) {
            throw HttpStatusException.conflict("请勿重复签到！");
        }
        logDTO.setDistance(-1.0);
        logDTO.setCourseId(task.getCourseId());
        
        /* 5. 计算经验值 */
        switch (logDTO.getStatus()) {
            case STATUS_NORMAL:
                /* 如果是教师操作应该不限制操作,所以可以传空的经纬度，但是学生的话必须传 */
                if (logDTO.getLatitude() != null || logDTO.getLongitude() != null) {
                    /* 计算签到距离*/
                    double distance = distanceSimplify(
                            logDTO.getLatitude(), logDTO.getLongitude(),
                            task.getLatitude(), task.getLongitude());
                    logDTO.setDistance(distance);
                    
                    if (distance > 1000) {
                        throw HttpStatusException.badRequest("签到失败：你太远了！" + distance + "m");
                    }
                    logDTO.setDistance(distance);
                }
                
                logDTO.setExperience(2);
                break;
            case STATUS_LEAVE:
            case STATUS_LATE:
                logDTO.setExperience(1);
                break;
            default:
                logDTO.setExperience(0);
        }
        
        CheckInLog checkInLog = dto2log(logDTO);
        checkInLog.createBy(operator.getId());
        logMapper.insert(checkInLog);
        logDTO.setId(checkInLog.getId());
        
        return logDTO;
    }
    
    /**
     * 创建签到任务/发起签到
     *
     * @param checkInTaskDTO 签到信息
     * @return 签到成功的信息
     */
    @Override
    public CheckInTaskDTO createCheckInTask(CheckInTaskDTO checkInTaskDTO) {
        
        
        log.info("创建签到任务:{}", checkInTaskDTO);
        
        Long courseId = checkInTaskDTO.getCourseId();
        Optional<Boolean> exists = courseMapper.exists(courseId);
        if (!exists.orElse(false)) {
            throw HttpStatusException.notFound("找不到班课！");
        }
        CheckInTask current = taskMapper.current(courseId, LocalDateTime.now());
        if (current != null) {
            throw HttpStatusException.conflict("当前有正在进行的签到！");
        }
        CheckInTask checkInTask = dto2task(checkInTaskDTO);
        checkInTask.createBy(checkInTaskDTO.getOperatorId());
        taskMapper.insert(checkInTask);
        return getTask(checkInTask.getId());
    }
    
    /**
     * 结束签到
     *
     * @param operator 修改人
     * @param taskId   任务id
     */
    @Override
    public void endCheckInTask(User operator, long taskId) {
        Optional<Boolean> exists = taskMapper.exists(taskId);
        if (!exists.orElse(false)) {
            throw HttpStatusException.notFound();
        }
        CheckInTask task = new CheckInTask();
        task.setId(taskId);
        LocalDateTime deadline = task.getDeadline();
        LocalDateTime now = LocalDateTime.now();
        // 如果结束时间在当前时间之前则不对其进行操作
        if (deadline == null || deadline.isAfter(now)) {
            task.setDeadline(now);
            task.updateBy(operator.getId());
            taskMapper.updateById(task);
        }
    }
    
    /**
     * 修改某个任务下某个学生的签到情况
     *
     * @param modifier 修改人
     * @param taskId   签到任务id
     * @param uid      学生id
     * @param status   签到状态
     * @return 修改后的记录dto
     */
    @Override
    public CheckInLogDTO modifyCheckInStatus(User modifier, long taskId, long uid, int status) {
        // 1. 检查班任务是否存在
        CheckInTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw HttpStatusException.notFound("签到任务不存在!");
        }
        // 2. 检查是否加入班课
        Boolean elected = userCourseMapper.isElected(uid, task.getCourseId());
        if (elected == null) {
            throw HttpStatusException.badRequest("未加入该班课！");
        }
        // 3. 检查是否存在记录
        CheckInLog checkInLog = logMapper.selectByTaskAndStu(taskId, uid);
        int experience = getExperience(status);
        if (checkInLog == null) {
            checkInLog = new CheckInLog();
            checkInLog.setUid(uid);
            checkInLog.setTaskId(taskId);
            checkInLog.setCourseId(task.getCourseId());
            checkInLog.setDistance(-1.0);
            checkInLog.setStatus(status);
            checkInLog.setExperience(experience);
            logMapper.insert(checkInLog);
        } else {
            checkInLog.setStatus(status);
            checkInLog.setExperience(experience);
            logMapper.updateById(checkInLog);
        }
        return getLog(checkInLog.getId());
    }
    
    /**
     * 获取某个课程当前的签到任务，如果没有签到任务会返回404
     *
     * @param courseId 课程id
     * @return 当前的签到任务
     */
    @Override
    public CheckInTaskDTO getCurrentCheckInTask(long courseId) {
        
        CheckInTask task = taskMapper.current(courseId, LocalDateTime.now());
        if (task == null) {
            throw HttpStatusException.notFound("当前未发起签到或已经截止！");
        }
        
        return task2dto(task);
    }
    
    /**
     * 获取未签到的学生信息
     *
     * @param taskId 任务 id
     * @return 未签到的信息
     */
    @Override
    public PageWrapper<CheckInResultDTO> unchecked(long taskId) {
        // 1. 获取已签到的信息
        List<Long> checkedStu = logMapper.listChecked(taskId);
        List<Long> allStu = userCourseMapper.listElected(taskId);
        // 2. 计算未签到成员
        
        
        List<CheckInResultDTO> uncheckStuDTO = allStu.stream()
                .filter(id -> !checkedStu.contains(id))
                .map(this::getUncheckResult)
                .collect(Collectors.toList());
        
        return PageWrapper.singlePage(uncheckStuDTO);
    }
    
    private CheckInResultDTO getUncheckResult(Long uid) {
        CheckInResultDTO resultDTO = new CheckInResultDTO();
        resultDTO.setUid(uid);
        User userById = userService.getUserById(uid);
        resultDTO.setStuId(userById.getAcademicId());
        resultDTO.setStuName(userById.getRealName());
        resultDTO.setStatus(STATUS_ABSENCE);
        resultDTO.setStatusName(getStatusName(STATUS_ABSENCE));
        resultDTO.setDistance(-1.0);
        return resultDTO;
    }
    
    private String getStatusName(int status) {
        
        switch (status) {
            case STATUS_NORMAL:
                return "已签到";
            case STATUS_LEAVE:
                return "请假";
            case STATUS_LATE:
                return "迟到";
            case STATUS_ABSENCE:
                return "缺勤";
        }
        return "未知";
    }
    
    private int getExperience(int status) {
        SysParameterDTO expDto = sysParamService.getSystemParam("sys_check_in_exp");
        int exp = Integer.parseInt(expDto.getValue());
        switch (status) {
            case STATUS_NORMAL:
                break;
            case STATUS_LEAVE:
            case STATUS_LATE:
                exp /= 2;
                break;
            default:
                exp = 0;
        }
        return exp;
    }
    
    private CheckInLog dto2log(CheckInLogDTO logDTO) {
        CheckInLog checkInLog = new CheckInLog();
        checkInLog.setLatitude(logDTO.getLatitude());
        checkInLog.setLongitude(logDTO.getLongitude());
        checkInLog.setTaskId(logDTO.getTaskId());
        checkInLog.setUid(logDTO.getUid());
        checkInLog.setDistance(logDTO.getDistance());
        checkInLog.setId(logDTO.getId());
        checkInLog.setExperience(logDTO.getExperience());
        checkInLog.setCourseId(logDTO.getCourseId());
        
        return checkInLog;
    }
    
    private CheckInLogDTO log2dto(CheckInLog checkInLog) {
        CheckInLogDTO logDTO = new CheckInLogDTO();
        logDTO.setId(checkInLog.getId());
        logDTO.setTaskId(checkInLog.getTaskId());
        logDTO.setLongitude(checkInLog.getLongitude());
        logDTO.setLatitude(checkInLog.getLatitude());
        logDTO.setDistance(checkInLog.getDistance());
        // 查找学号和姓名
        long uid = checkInLog.getUid();
        logDTO.setUid(uid);
        User userById = userService.getUserById(uid);
        logDTO.setStuId(userById.getAcademicId());
        logDTO.setStuName(userById.getRealName());
        return logDTO;
    }
    
    private CheckInResultDTO log2result(CheckInLog checkInLog) {
        CheckInResultDTO resultDTO = new CheckInResultDTO();
        resultDTO.setUid(checkInLog.getUid());
        int status = checkInLog.getStatus();
        resultDTO.setStatus(status);
        resultDTO.setStatusName(getStatusName(status));
        resultDTO.setExperience(checkInLog.getExperience());
        resultDTO.setLongitude(checkInLog.getLongitude());
        resultDTO.setLatitude(checkInLog.getLatitude());
        resultDTO.setDistance(checkInLog.getDistance());
        resultDTO.setCheckTime(checkInLog.getCreateTime());
        // 查找学号和姓名
        long uid = checkInLog.getUid();
        resultDTO.setUid(uid);
        User userById = userService.getUserById(uid);
        resultDTO.setStuId(userById.getAcademicId());
        resultDTO.setStuName(userById.getRealName());
        
        return resultDTO;
    }
    
    private CheckInTaskDTO task2dto(CheckInTask record) {
        CheckInTaskDTO checkInTaskDTO;
        checkInTaskDTO = new CheckInTaskDTO();
        checkInTaskDTO.setId(record.getId());
        checkInTaskDTO.setType(record.getType());
        LocalDateTime deadline = record.getDeadline();
        checkInTaskDTO.setDeadline(deadline);
        checkInTaskDTO.setStartTime(record.getCreateTime());
        checkInTaskDTO.setCourseId(record.getCourseId());
        checkInTaskDTO.setDescription(record.getDescription());
        checkInTaskDTO.setLongitude(record.getLongitude());
        checkInTaskDTO.setLatitude(record.getLatitude());
        checkInTaskDTO.setParam(record.getParam());
        Long operator = record.getCreator();
        checkInTaskDTO.setOperatorId(operator);
        checkInTaskDTO.setOperatorName(userService.getRealNameById(operator));
        return checkInTaskDTO;
    }
    
    private CheckInTask dto2task(CheckInTaskDTO dto) {
        CheckInTask entity;
        entity = new CheckInTask();
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        
        entity.setDeadline(dto.getDeadline());
        
        entity.setCourseId(dto.getCourseId());
        entity.setDescription(dto.getDescription());
        
        if (dto.getLongitude() == null) {
            dto.setLongitude(0.0);
        }
        if (dto.getLatitude() == null) {
            dto.setLatitude(0.0);
        }
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setParam(dto.getParam());
        entity.setUpdater(dto.getOperatorId());
        return entity;
    }
    
    /**
     * 距离计算
     *
     * @return 距离
     */
    public static double distanceSimplify(double lat1, double lng1, double lat2, double lng2) {
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        double Lx = toRadians(dx) * 6367000.0 * Math.cos(toRadians(b)); // 东西距离
        double Ly = 6367000.0 * toRadians(dy); // 南北距离
        return Math.sqrt(Lx * Lx + Ly * Ly);  // 用平面的矩形对角距离公式计算总距离
    }
}
