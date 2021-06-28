package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.PermissionDTO;

public interface IPermissionService {
    /**
     * 获取权限列表
     *
     * @param pageSort 分页
     * @return 权限
     */
    PageWrapper<PermissionDTO> listPerms(PageSort pageSort);
    
    /**
     * 创建权限
     *
     * @param dto 待创建
     * @return 创建结果
     */
    PermissionDTO create(PermissionDTO dto);
    
    /**
     * 删除权限
     *
     * @param permId 权限id
     */
    void delete(long permId);
    
    /**
     * 修改权限信息
     *
     * @param dto 待修改
     * @return 修改结果
     */
    PermissionDTO modify(PermissionDTO dto);
    
    /**
     * 按id获取权限信息
     *
     * @param permId 权限id
     * @return 权限信息
     */
    PermissionDTO getPerm(long permId);
}
