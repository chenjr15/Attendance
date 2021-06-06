package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.entity.SystemParam;
import dev.chenjr.attendance.service.dto.InputSysParameterDTO;
import dev.chenjr.attendance.service.dto.RestResponse;

import java.util.List;

public class SysParamService implements dev.chenjr.attendance.service.SysParamService {
    /**
     * 获取所有系统参数，带分页参数
     *
     * @param curPage  当前页面
     * @param pageSize 页面大小
     * @return 系统参数列表
     */
    @Override
    public RestResponse<List<SystemParam>> getAllSystemParams(long curPage, long pageSize) {
        return null;
    }

    /**
     * 获取所有系统参数，不带分页参数
     *
     * @return 系统参数列表
     */
    @Override
    public RestResponse<List<SystemParam>> getAllSystemParams() {
        return null;
    }

    /**
     * 获取某个系统参数的详细信息
     *
     * @param paramCode 系统参数英文标识
     * @return 系统参数
     */
    @Override
    public RestResponse<SystemParam> getSystemParam(String paramCode) {
        return null;
    }

    /**
     * 修改系统参数
     *
     * @param sysParameter 要修改的系统参数
     * @return 修改结果
     */
    @Override
    public RestResponse<?> updateSystemParams(InputSysParameterDTO sysParameter) {
        return null;
    }
}
