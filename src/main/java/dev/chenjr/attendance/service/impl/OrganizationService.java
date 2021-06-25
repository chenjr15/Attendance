package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Organization;
import dev.chenjr.attendance.dao.mapper.OrganizationMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.IOrganizationService;
import dev.chenjr.attendance.service.dto.OrganizationDTO;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OrganizationService implements IOrganizationService {
    public static final String ORG_TYPE = "org_type";
    //    private Map<String, Integer> orgValMap;
//    private Map<Integer, String> valOrgMap;
    @Autowired
    IDictionaryService dictionaryService;
    @Autowired
    OrganizationMapper organizationMapper;
    
    int getOrgValue(String orgCode) {
        Map<Integer, String> cacheDict = dictionaryService.getCacheDict(ORG_TYPE);
        for (Map.Entry<Integer, String> valName : cacheDict.entrySet()) {
            if (valName.getValue().equals(orgCode)) {
                return valName.getKey();
            }
        }
        
        return 0;
        
    }
    
    String getOrgType(Integer orgValue) {
        if (orgValue == null) {
            orgValue = 0;
        }
        return dictionaryService.getCacheDictDetail(ORG_TYPE, orgValue);
    }
    
    
    @Override
    public PageWrapper<OrganizationDTO> listPage(String orgType, PageSort pageSort) {
        
        int orgValue = getOrgValue(orgType);
        if (orgValue < 0) {
            throw HttpStatusException.notFound("找不到该类型的组织结构:" + orgType);
        }
        Page<Organization> page = pageSort.getPage();
        
        QueryWrapper<Organization> wr = new QueryWrapper<Organization>()
                .eq(ORG_TYPE, orgValue)
                .eq(StringUtil.toUnderlineCase("parentId"), 0);
        
        wr = pageSort.buildQueryWrapper(wr, "name");
        page = organizationMapper.selectPage(page, wr);
        
        PageWrapper<OrganizationDTO> pageWrapper = PageWrapper.fromIPage(page);
        List<Organization> records = page.getRecords();
        
        if (records != null && records.size() != 0) {
            List<OrganizationDTO> orgDtoList = new ArrayList<>(records.size());
            for (Organization record : records) {
                OrganizationDTO organizationDTO = organization2DTO(record);
                orgDtoList.add(organizationDTO);
            }
            pageWrapper.setContent(orgDtoList);
        }
        
        return pageWrapper;
    }
    
    /**
     * 不查找其孩子节点的fetch，!会返回null！
     *
     * @param orgId 节点id
     * @return 节点信息
     */
    @Override
    public OrganizationDTO fetchItSelf(long orgId) {
        Organization organization = organizationMapper.selectById(orgId);
        if (organization == null) {
            return null;
        }
        return organization2DTO(organization);
    }
    
    
    /**
     * 获取某个节点的信息, 返回一级子节点
     *
     * @param orgId 节点id
     * @return 节点信息
     */
    @Override
    public OrganizationDTO fetch(long orgId) {
        Organization organization = organizationMapper.selectById(orgId);
        if (organization == null) {
            throw HttpStatusException.notFound();
        }
        OrganizationDTO organizationDTO = organization2DTO(organization);
        QueryWrapper<Organization> wr = new QueryWrapper<Organization>()
                .eq("parent_id", organization.getId());
        List<Organization> childrenRecords = organizationMapper.selectList(wr);
//        log.info("childrenRecords:{}", childrenRecords);
        if (childrenRecords != null && childrenRecords.size() != 0) {
            List<OrganizationDTO> orgChildren = new ArrayList<>(childrenRecords.size());
            for (Organization child : childrenRecords) {
                OrganizationDTO childDTO = organization2DTO(child);
                int childrenCount = organizationMapper.childrenCount(child.getId());
                childDTO.setChildrenCount(childrenCount);
                orgChildren.add(childDTO);
            }
            organizationDTO.setChildrenCount(orgChildren.size());
            organizationDTO.setChildren(orgChildren);
        }
        
        return organizationDTO;
    }
    
    /**
     * 获取某个节点的信息, 并返回一级子节点
     *
     * @param orgId    节点id
     * @param pageSort 子节点的分页排序筛选信息
     * @return 节点信息和分页后的子节点信息
     */
    @Override
    public OrganizationDTO listChildren(long orgId, PageSort pageSort) {
        Organization organization = organizationMapper.selectById(orgId);
        if (organization == null) {
            throw HttpStatusException.notFound();
        }
        OrganizationDTO organizationDTO = organization2DTO(organization);
        
        QueryWrapper<Organization> wr = new QueryWrapper<Organization>()
                .eq("parent_id", organization.getId());
        
        wr = pageSort.buildQueryWrapper(wr, "name");
        
        Page<Organization> organizationPage = organizationMapper.selectPage(pageSort.getPage(), wr);
        List<Organization> childrenRecords = organizationPage.getRecords();
        //log.info("childrenRecords:{}", childrenRecords);
        
        List<OrganizationDTO> orgChildren = new ArrayList<>(childrenRecords.size());
        if (childrenRecords.size() != 0) {
            for (Organization child : childrenRecords) {
                OrganizationDTO childDTO = organization2DTO(child);
                int childrenCount = organizationMapper.childrenCount(child.getId());
                childDTO.setChildrenCount(childrenCount);
                orgChildren.add(childDTO);
            }
            organizationDTO.setChildrenCount(organizationPage.getTotal());
            PageWrapper<OrganizationDTO> pageWrapper = PageWrapper.fromList(organizationPage, orgChildren);
            organizationDTO.setChildrenWrapper(pageWrapper);
        }
        return organizationDTO;
    }
    
    /**
     * 修改某个节点信息，不改变其儿子节点
     *
     * @param orgDTO 要修改的信息
     */
    @Override
    public OrganizationDTO modify(OrganizationDTO orgDTO) {
        Optional<Boolean> exists = organizationMapper.exists(orgDTO.getId());
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        Organization newOne = dto2Organization(orgDTO);
        organizationMapper.updateById(newOne);
        return fetch(orgDTO.getId());
    }
    
    
    /**
     * 创建节点，会递归创建子类
     *
     * @param organizationDTO 待创建的节点
     * @return 创建成功的返回，和getOrg同样的返回
     */
    @Override
    public OrganizationDTO create(OrganizationDTO organizationDTO) {
        Organization parent = organizationMapper.selectById(organizationDTO.getParentId());
        if (parent != null) {
            organizationDTO.setParents(parent.getFullName());
        }
        long id = createOnly(organizationDTO, 0);
        // 可以用层次遍历，每一层一次插入
        return fetch(id);
    }
    
    public long createOnly(OrganizationDTO organizationDTO, int depth) {
        if (depth > 4) {
            // 限制四层递归
            return 0;
        }
//        organizationDTO.setId(null);
        Organization newOne = dto2Organization(organizationDTO);
        int insert = organizationMapper.insert(newOne);
        if (insert != 1) {
            throw new SuperException("Failed to create.");
        }
        
        List<OrganizationDTO> children = organizationDTO.getChildren();
        if (children != null) {
            for (OrganizationDTO child : children) {
                child.setParentId(newOne.getId());
                child.setParents(newOne.getFullName());
                if (child.getProvinceId() == null) {
                    child.setProvinceId(newOne.getProvinceId());
                }
                createOnly(child, depth + 1);
            }
        }
        return newOne.getId();
    }
    
    
    /**
     * 删除节点，不会级联删除儿子节点
     *
     * @param orgId 要删除id
     */
    @Override
    public void delete(long orgId) {
        Optional<Boolean> exists = organizationMapper.exists(orgId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        int i = organizationMapper.childrenCount(orgId);
        if (i != 0) {
            throw HttpStatusException.conflict("删除失败，请先删除子节点！");
        }
        organizationMapper.deleteById(orgId);
    }
    
    
    private Organization dto2Organization(OrganizationDTO orgDTO) {
        Organization newOne = new Organization();
        newOne.setId(orgDTO.getId());
        newOne.setCode(null);
        newOne.setName(orgDTO.getName());
        newOne.setComment(orgDTO.getComment());
        newOne.setProvinceId(orgDTO.getProvinceId());
        newOne.setParentId(orgDTO.getParentId());
        newOne.setParents(orgDTO.getParents());
//        newOne.setOrgType(getOrgValue(orgDTO.getOrgType()));
        return newOne;
    }
    
    private OrganizationDTO organization2DTO(Organization record) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(record.getId());
        dto.setParentId(record.getParentId());
        Long provinceId = record.getProvinceId();
        dto.setProvinceId(provinceId);
        dto.setName(record.getName());
        dto.setComment(record.getComment());
        dto.setParents(record.getParents());
//        dto.setOrgType(getOrgType(record.getOrgType()));
//        if (provinceId != null && provinceId != 0) {
//            OrganizationDTO province = fetchItSelf(provinceId);
//            if (province != null) {
//                dto.setProvince(province.getName());
//            } else {
//                dto.setProvince("UNKNOWN");
//            }
//        }
        
        return dto;
    }
}
