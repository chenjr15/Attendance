package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Organization;
import dev.chenjr.attendance.dao.mapper.OrganizationMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.IOrganizationService;
import dev.chenjr.attendance.service.dto.*;
import dev.chenjr.attendance.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OrganizationService implements IOrganizationService {
    public static final String ORG_TYPE = "org_type";
    private Map<String, Integer> orgValMap;
    private Map<Integer, String> valOrgMap;
    @Autowired
    IDictionaryService dictionaryService;
    @Autowired
    OrganizationMapper organizationMapper;

    int getOrgValue(String orgCode) {
        loadOrgValueMapping();
        return orgValMap.getOrDefault(orgCode, -1);

    }

    String getOrgType(Integer orgValue) {
        loadOrgValueMapping();
        if (orgValue == null) {
            orgValue = 0;
        }
        return valOrgMap.getOrDefault(orgValue, "");
    }


    /**
     * 返回指定类型的组织结构
     *
     * @param orgType  类型英文名
     * @param curPage  当前页面
     * @param pageSize 页面大小
     * @return 分页的组织结构数据
     */
    @Override
    public PageWrapper<OrganizationDTO> listPage(String orgType, long curPage, long pageSize) {
        int orgValue = getOrgValue(orgType);
        if (orgValue < 0) {
            throw HttpStatusException.notFound("找不到该类型的组织结构:" + orgType);
        }
        Page<Organization> page = new Page<>(curPage, pageSize);
        QueryWrapper<Organization> wr = new QueryWrapper<Organization>()
                .eq(ORG_TYPE, orgValue)
                .eq(StringUtil.toUnderlineCase("parentId"), 0);
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
        organizationDTO.setProvince("");

        QueryWrapper<Organization> wr = new QueryWrapper<Organization>()
                .eq("parent_id", organization.getId());
        List<Organization> records = organizationMapper.selectList(wr);
        if (records != null && records.size() != 0) {
            List<OrganizationDTO> orgChildren = new ArrayList<>(records.size());
            for (Organization record : records) {
                OrganizationDTO childDTO = organization2DTO(record);
                orgChildren.add(childDTO);
            }
            organizationDTO.setChildrenCount(orgChildren.size());
            organizationDTO.setChildren(orgChildren);
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
        organizationMapper.deleteById(orgId);
    }

    /**
     * 手动加载字典项中的数据变化
     */
    @Override
    public void loadOrgValueMapping() {
        if (orgValMap == null || valOrgMap == null) {
            // load orgType
            orgValMap = new TreeMap<>();
            valOrgMap = new TreeMap<>();
        } else {
            orgValMap.clear();
            valOrgMap.clear();
        }
        DictionaryDTO orgTypeDict = dictionaryService.getDictionaryByCode(ORG_TYPE);
        for (DictionaryDetailDTO detail : orgTypeDict.getDetails()) {
            String code = detail.getCode();
            Integer value = detail.getValue();
            orgValMap.put(code, value);
            valOrgMap.put(value, code);
        }
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
        newOne.setOrgType(getOrgValue(orgDTO.getOrgType()));
        return newOne;
    }

    private OrganizationDTO organization2DTO(Organization record) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(record.getId());
        dto.setParentId(record.getParentId());
        dto.setProvinceId(record.getProvinceId());
        dto.setName(record.getName());
        dto.setComment(record.getComment());
        dto.setParents(record.getParents());
        dto.setOrgType(getOrgType(record.getOrgType()));
        return dto;
    }
}
