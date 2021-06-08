package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Dictionary;
import dev.chenjr.attendance.dao.entity.DictionaryDetail;
import dev.chenjr.attendance.dao.mapper.DictionaryDetailMapper;
import dev.chenjr.attendance.dao.mapper.DictionaryMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.dto.DictionaryDTO;
import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService implements IDictionaryService {
    @Autowired
    DictionaryMapper dictMapper;
    @Autowired
    DictionaryDetailMapper detailMapper;

    /**
     * 添加新的数据字典和明细项
     *
     * @param dictionaryDTO 前端过来的数据字典dto
     * @return 创建的数据字典
     */
    @Override
    @Transactional
    public DictionaryDTO addDictionary(DictionaryDTO dictionaryDTO) {
        dictionaryDTO.setId(null);
        final Dictionary dictionary = dto2Dict(dictionaryDTO);
        int insert = dictMapper.insert(dictionary);
        if (insert == 0) {
            throw new RuntimeException("Fail to insert dict!");
        }
        // 获取创建后的数据
        final DictionaryDTO createdDTO = this.getDictionaryByCode(dictionaryDTO.getCode());
        createdDTO.setDetails(new ArrayList<>());
        boolean gotDefault = false;
        // 逐条创建明细
        for (DictionaryDetailDTO detailDTO : dictionaryDTO.getDetails()) {
            if (!gotDefault && detailDTO.getIsDefault()) {
                gotDefault = true;
            }
            final DictionaryDetailDTO createdDetailDTO = this.addDictionaryDetailNoCheck(createdDTO.getId(), detailDTO);
            createdDTO.getDetails().add(createdDetailDTO);
        }
        return createdDTO;

    }

    /**
     * 给某个数据字典添加明细
     *
     * @param dictId    数据字典id
     * @param detailDTO 需要添加的明细
     * @return 添加后的结果
     */
    @Override
    @Transactional
    public DictionaryDetailDTO addDictionaryDetail(long dictId, DictionaryDetailDTO detailDTO) {
        Boolean exists = this.dictMapper.exists(dictId);
        if (exists == null || !exists) {
            throw HttpStatusException.notFound();
        }
        return addDictionaryDetailNoCheck(dictId, detailDTO);
    }

    public DictionaryDetailDTO addDictionaryDetailNoCheck(long dictId, DictionaryDetailDTO detailDTO) {
        DictionaryDetail dictionaryDetail = dto2Detail(dictId, detailDTO);
        int insert = detailMapper.insert(dictionaryDetail);
        // TODO 处理插入失败
        return detailDTO;
    }

    /**
     * 分页获取数据字典, 不返回明细项
     *
     * @param curPage  当前页,1开始
     * @param pageSize 页面大小
     * @return 数据和分页信息
     */
    @Override
    public PageWrapper<DictionaryDTO> listDictionary(long curPage, long pageSize) {
        Page<Dictionary> page = new Page<>(curPage, pageSize);
        dictMapper.selectPage(page, null);
        PageWrapper<DictionaryDTO> pageWrapper = PageWrapper.fromIPage(page);
        List<DictionaryDTO> dictionaryList = page.getRecords().stream().map(this::dict2dto).collect(Collectors.toList());
        pageWrapper.setContent(dictionaryList);
        return pageWrapper;
    }

    /**
     * 获取某个数据字典的详细信息，包括字典明细项
     *
     * @param dictId 数据字典id
     * @return 数据字典详细信息
     */
    @Override
    public DictionaryDTO getDictionary(long dictId) {
        Dictionary dictionary = dictMapper.selectById(dictId);
        if (dictionary == null) {
            throw HttpStatusException.notFound("Can't find Dictionary by id:" + dictId);
        }
        DictionaryDTO dto = dict2dto(dictionary);
        QueryWrapper<DictionaryDetail> wr = new QueryWrapper<DictionaryDetail>().eq("dictionary_id", dictId);
        
        List<DictionaryDetail> dictionaryDetails = detailMapper.selectList(wr);

        List<DictionaryDetailDTO> detailDTOS = new ArrayList<>(dictionaryDetails.size());
        for (DictionaryDetail detail : dictionaryDetails) {
            DictionaryDetailDTO detailDTO = detail2DTO(detail);
            detailDTOS.add(detailDTO);
            if (detail.getDefaultItem()) {
                dto.setDefaultValue(detail.getItemValue());
            }
        }
        dto.setDetails(detailDTOS);
        return dto;
    }


    /**
     * 修改数据字典(不修改明细！)
     *
     * @param dictionaryDTO 修改的数据
     * @return 修改后的数据
     */
    @Override
    public DictionaryDTO modifyDictionary(DictionaryDTO dictionaryDTO) {
        Boolean exists = dictMapper.exists(dictionaryDTO.getId());
        if (exists == null || !exists) {
            throw HttpStatusException.notFound("Can not found dict by id:" + dictionaryDTO.getId().toString());
        }
        Dictionary dictionary = dto2Dict(dictionaryDTO);
        this.dictMapper.update(dictionary, null);
        return this.getDictionary(dictionaryDTO.getId());
    }

    /**
     * 修改某个数据字典的某个明细项
     *
     * @param dictId    数据字典id
     * @param detailDTO 要修改的明细
     * @return 修改后的完整的数据字典
     */
    @Override
    public DictionaryDTO modifyDictionaryDetail(long dictId, DictionaryDetailDTO detailDTO) {

        Boolean exists = detailMapper.exists(detailDTO.getId());
        if (exists == null || !exists) {
            throw HttpStatusException.notFound();
        }
        DictionaryDetail dictionaryDetail = dto2Detail(dictId, detailDTO);
        this.detailMapper.update(dictionaryDetail, null);
        return this.getDictionary(dictId);
    }

    /**
     * 删除指定的数据字典明细
     *
     * @param detailId 要删除的明细id
     */
    @Override
    public void deleteDictionaryDetail(long detailId) {
        this.detailMapper.deleteByDictId(detailId);
    }

    /**
     * 删除指定的数据字典
     *
     * @param dictId 要删除的数据字典id
     */
    @Override
    public void deleteDictionary(long dictId) {
        // 1. 先删掉关联的字典明细
        // 2. 再把本体删掉
        this.detailMapper.deleteByDictId(dictId);
        this.dictMapper.deleteById(dictId);
    }

    /**
     * 通过英文表示获取数据字典
     *
     * @param code 英文标识
     * @return 数据字典
     */
    @Override
    public DictionaryDTO getDictionaryByCode(String code) {
        Dictionary dictionary = dictMapper.getByCode(code);
        if (dictionary == null) {
            throw HttpStatusException.notFound();
        }
        DictionaryDTO dto = dict2dto(dictionary);
        List<DictionaryDetailDTO> dictionaryDetails = this.getDictionaryDetails(dictionary.getId());
        dto.setDetails(dictionaryDetails);
        return dto;
    }

    /**
     * 查询某个字典的 所有字典明细
     *
     * @param dictId 字典id
     * @return 明细列表
     */
    @Override
    public List<DictionaryDetailDTO> getDictionaryDetails(long dictId) {
        List<DictionaryDetail> details = detailMapper.getByDictId(dictId);
        return details.stream().map(this::detail2DTO).collect(Collectors.toList());
    }


    private Dictionary dto2Dict(DictionaryDTO dictionaryDTO) {
        final Dictionary dictionary = new Dictionary();
        dictionary.setName(dictionaryDTO.getName());
        dictionary.setDescription(dictionaryDTO.getDescription());
        dictionary.setCode(dictionaryDTO.getCode());
        return dictionary;
    }

    private DictionaryDTO dict2dto(Dictionary dictionary) {
        final DictionaryDTO dto = new DictionaryDTO();
        dto.setName(dictionary.getName());
        dto.setDescription(dictionary.getDescription());
        dto.setCode(dictionary.getCode());
        dto.setId(dictionary.getId());

        return dto;
    }

    private DictionaryDetailDTO detail2DTO(DictionaryDetail detail) {
        DictionaryDetailDTO detailDTO = new DictionaryDetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setValue(detail.getItemValue());
        detailDTO.setName(detail.getItemName());
        detailDTO.setIsDefault(detail.getDefaultItem());
        detailDTO.setShouldDisplay(detail.getDisplay());
        detailDTO.setOrder(detail.getOrderValue());
        return detailDTO;
    }

    private DictionaryDetail dto2Detail(Long dictId, DictionaryDetailDTO detailDTO) {
        DictionaryDetail dictionaryDetail = new DictionaryDetail();
        dictionaryDetail.setDictionaryId(dictId);
        dictionaryDetail.setDisplay(detailDTO.getShouldDisplay());
        dictionaryDetail.setDefaultItem(detailDTO.getIsDefault());
        dictionaryDetail.setItemName(detailDTO.getName());
        dictionaryDetail.setItemValue(detailDTO.getValue());
        dictionaryDetail.setOrderValue(detailDTO.getOrder());
        return dictionaryDetail;
    }
}
