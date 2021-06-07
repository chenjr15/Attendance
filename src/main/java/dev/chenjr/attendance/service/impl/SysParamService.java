package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.SystemParam;
import dev.chenjr.attendance.dao.enums.ParamEnum;
import dev.chenjr.attendance.dao.mapper.SystemParamMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.ISysParamService;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.SysParameterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysParamService implements ISysParamService {

    @Autowired
    SystemParamMapper systemParamMapper;


    /**
     * 获取所有系统参数，带分页参数
     *
     * @param curPage  当前页面
     * @param pageSize 页面大小
     * @return 系统参数列表
     */
    @Override
    public PageWrapper<SysParameterDTO> getAllSystemParams(long curPage, long pageSize) {
        Page<SystemParam> page = new Page<>(curPage, pageSize);
        page = systemParamMapper.selectPage(page, null);
        List<SysParameterDTO> collect = page.getRecords().stream().map(SysParamService::sysParamToDTO).collect(Collectors.toList());
        PageWrapper<SysParameterDTO> dtoPage = PageWrapper.fromIPage(page);
        dtoPage.setContent(collect);
        return dtoPage;
    }

    /**
     * 获取所有系统参数，不带分页参数
     *
     * @return 系统参数列表
     */
    @Override
    public PageWrapper<SysParameterDTO> getAllSystemParams() {
        return getAllSystemParams(1, 10);
    }

    /**
     * 获取某个系统参数的详细信息
     *
     * @param paramCode 系统参数英文标识
     * @return 系统参数
     */
    @Override
    public SysParameterDTO getSystemParam(String paramCode) {

        SystemParam systemParam = systemParamMapper.getByParamCode(paramCode);
        if (systemParam == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND);
        }
        return sysParamToDTO(systemParam);
    }

    /**
     * 修改系统参数
     *
     * @param dto 要修改的系统参数
     */
    @Override
    public void updateSystemParams(SysParameterDTO dto) {
        SystemParam sysParameter = dtoToSysParam(dto);

        systemParamMapper.update(sysParameter, null);
    }


    /**
     * 初始化测试数据
     */
    @Override
    @Transactional
    public void initSysParams() {
        SysParameterDTO checkInRange = new SysParameterDTO();
        checkInRange.setParamType(ParamEnum.DOUBLE.getValue());
        checkInRange.setCode("sys_check_in_range");
        checkInRange.setValue("1000");
        checkInRange.setName("最大签到范围");
        checkInRange.setDescription("超过该范围无法签到");

        this.createSystemParams(checkInRange);

        SysParameterDTO checkInExp = new SysParameterDTO();
        checkInExp.setParamType(ParamEnum.DOUBLE.getValue());
        checkInExp.setCode("sys_check_in_exp");
        checkInExp.setValue("2");
        checkInExp.setName("签到经验值");
        checkInExp.setDescription("每次签到的经验值");
        this.createSystemParams(checkInExp);

        SysParameterDTO checkInLateExp = new SysParameterDTO();
        checkInLateExp.setParamType(ParamEnum.DOUBLE.getValue());
        checkInLateExp.setCode("sys_check_late_exp");
        checkInLateExp.setValue("1");
        checkInLateExp.setName("迟到经验值");
        checkInLateExp.setDescription("签到时标记为迟到的经验值");
        this.createSystemParams(checkInLateExp);


    }

    /**
     * 清空(删除所有的)系统参数数据库
     */
    @Override
    public void deleteAll() {
        systemParamMapper.delete(null);
    }

    /**
     * 删除指定的系统参数
     *
     * @param paramCode 系统参数标识
     */
    @Override
    public void deleteByCode(String paramCode) {
        systemParamMapper.deleteByParamCode(paramCode);
    }

    /**
     * 创建系统参数
     *
     * @param dto 系统参数细节
     */
    @Override
    public void createSystemParams(SysParameterDTO dto) {
        SystemParam systemParam = dtoToSysParam(dto);
        systemParamMapper.insert(systemParam);
    }


    private SystemParam dtoToSysParam(SysParameterDTO dto) {
        SystemParam sysParameter = new SystemParam();
        sysParameter.setParamCode(dto.getCode());
        sysParameter.setParamName(dto.getName());
        sysParameter.setDescription(dto.getDescription());
        sysParameter.setParamRange(dto.getRange());
        sysParameter.setValue(dto.getValue());
        sysParameter.setParamType(dto.getParamType());
        return sysParameter;
    }

    private static SysParameterDTO sysParamToDTO(SystemParam systemParam) {
        SysParameterDTO dto = new SysParameterDTO();
        dto.setCode(systemParam.getParamCode());
        dto.setParamType(systemParam.getParamType());
        dto.setName(systemParam.getParamName());
        dto.setRange(systemParam.getParamRange());
        dto.setValue(systemParam.getValue());
        dto.setDescription(systemParam.getDescription());
        return dto;
    }

}
