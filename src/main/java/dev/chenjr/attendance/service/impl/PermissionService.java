package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Permission;
import dev.chenjr.attendance.dao.mapper.PermissionMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IPermissionService;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.PermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService implements IPermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    
    /**
     * 获取权限列表
     *
     * @param pageSort 分页
     * @return 权限
     */
    @Override
    public PageWrapper<PermissionDTO> listPerms(PageSort pageSort) {
        Page<Permission> page = pageSort.getPage();
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw = pageSort.buildQueryWrapper(qw, "name");
        page = permissionMapper.selectPage(page, qw);
        List<PermissionDTO> collected = page.getRecords().stream().map(this::perm2dto).collect(Collectors.toList());
        return PageWrapper.fromList(page, collected);
    }
    
    
    /**
     * 按id获取权限信息
     *
     * @param permId 权限id
     * @return 权限信息
     */
    @Override
    public PermissionDTO getPerm(long permId) {
        Permission permission = this.permissionMapper.selectById(permId);
        if (permission == null) {
            throw HttpStatusException.notFound();
        }
        return perm2dto(permission);
    }
    
    /**
     * 创建权限
     *
     * @param dto 待创建
     * @return 创建结果
     */
    @Override
    public PermissionDTO create(PermissionDTO dto) {
        Permission permission = dto2perm(dto);
        permissionMapper.insert(permission);
        return getPerm(dto.getId());
    }
    
    
    /**
     * 删除权限
     *
     * @param permId 权限id
     */
    @Override
    public void delete(long permId) {
        permissionMapper.deleteById(permId);
    }
    
    /**
     * 修改权限信息
     *
     * @param dto 待修改
     * @return 修改结果
     */
    @Override
    public PermissionDTO modify(PermissionDTO dto) {
        return null;
    }
    
    private PermissionDTO perm2dto(Permission perm) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(perm.getId());
        dto.setName(perm.getPermName());
        dto.setType(perm.getType());
        return dto;
    }
    
    private Permission dto2perm(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setPermName(dto.getName());
        permission.setType(dto.getType());
        permission.setId(dto.getId());
        permission.setParentId(0L);
        return permission;
    }
    
}
