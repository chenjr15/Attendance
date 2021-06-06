package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.SystemParam;
import dev.chenjr.attendance.dao.enums.ParamEnum;
import dev.chenjr.attendance.dao.mapper.SystemParamMapper;
import dev.chenjr.attendance.service.ISysParamService;
import dev.chenjr.attendance.service.dto.SysParameterDTO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<SysParameterDTO> getAllSystemParams(long curPage, long pageSize) {
        Page<SystemParam> page = new Page<>(curPage, pageSize);
        systemParamMapper.selectPage(page, null);
        return page.getRecords().stream().map(SysParamService::sysParamToDTO).collect(Collectors.toList());
    }

    /**
     * 获取所有系统参数，不带分页参数
     *
     * @return 系统参数列表
     */
    @Override
    public List<SysParameterDTO> getAllSystemParams() {
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

        QueryWrapper<SystemParam> wrapper = new QueryWrapper<SystemParam>().eq("param_code", paramCode);
        SystemParam systemParam = systemParamMapper.selectOne(wrapper);
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
    public void initSysParams() {
        SysParameterDTO checkInRange = new SysParameterDTO();
        checkInRange.setParamType(ParamEnum.DOUBLE.getValue());
        checkInRange.setCode("sys_check_in_range");
        checkInRange.setValue("1000");
        checkInRange.setName("最大签到范围");

        this.createSystemParams(checkInRange);

        SysParameterDTO checkInExp = new SysParameterDTO();
        checkInExp.setParamType(ParamEnum.DOUBLE.getValue());
        checkInExp.setCode("sys_check_in_exp");
        checkInExp.setValue("10");
        checkInExp.setName("每次签到的经验值");

        this.createSystemParams(checkInRange);


    }

    /**
     * 创建系统参数
     *
     * @param dto 系统参数细节
     */
    private void createSystemParams(SysParameterDTO dto) {
        SystemParam systemParam = dtoToSysParam(dto);
        systemParamMapper.insert(systemParam);
    }

    @NotNull
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
