package dev.chenjr.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.chenjr.attendance.dao.entity.Dictionary;
import dev.chenjr.attendance.dao.entity.DictionaryDetail;
import dev.chenjr.attendance.dao.mapper.DictionaryDetailMapper;
import dev.chenjr.attendance.dao.mapper.DictionaryMapper;
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
        final Dictionary dictionary = dto2Dict(dictionaryDTO);
        dictMapper.insert(dictionary);
        // 获取创建后的数据
        final DictionaryDTO createdDTO = this.getDictionaryByCode();
        createdDTO.setDetails(new ArrayList<>());
        boolean gotDefault = false;
        // 逐条创建明细
        for (DictionaryDetailDTO detailDTO : dictionaryDTO.getDetails()) {
            if (!gotDefault && detailDTO.getIsDefault()) {
                gotDefault = true;
            }
            final DictionaryDetailDTO createdDetailDTO = this.addDictionaryDetail(createdDTO.getId(), detailDTO);
            createdDTO.getDetails().add(createdDetailDTO);
        }
        return createdDTO;

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
    public DictionaryDTO getDictionary(Long dictId) {
        Dictionary dictionary = dictMapper.selectById(dictId);
        QueryWrapper<DictionaryDetail> wr = new QueryWrapper<DictionaryDetail>().eq("dictionary_id", dictId);
        List<DictionaryDetail> dictionaryDetails = detailMapper.selectList(wr);
        List<DictionaryDetailDTO> detailDTOS = dictionaryDetails.stream()
                .map(this::detail2DTO).collect(Collectors.toList());
        DictionaryDTO dto = dict2dto(dictionary);
        dto.setDetails(detailDTOS);
        return dto;
    }


    /**
     * 修改数据字典(全量修改)
     *
     * @param dictId        数据字典id
     * @param dictionaryDTO 修改的数据
     * @return 修改后的数据
     */
    @Override
    public DictionaryDTO modifyDictionary(Long dictId, DictionaryDTO dictionaryDTO) {
        return null;
    }

    /**
     * 修改某个数据字典的某个明细项
     *
     * @param dictId    数据字典id
     * @param detailDTO 要修改的明细
     * @return 修改后的完整的数据字典
     */
    @Override
    public DictionaryDTO modifyDictionaryDetail(Long dictId, DictionaryDetailDTO detailDTO) {
        return null;
    }

    /**
     * 删除指定的数据字典
     *
     * @param dictId 要删除的数据字典id
     */
    @Override
    public void deleteDictionary(Long dictId) {

    }

    /**
     * 通过英文表示获取数据字典
     *
     * @return 数据字典
     */
    @Override
    public DictionaryDTO getDictionaryByCode() {
        return null;
    }

    /**
     * 给某个数据字典添加明细
     *
     * @param dictId    数据字典id
     * @param detailDTO 需要添加的明细
     * @return 添加后的结果
     */
    @Override
    public DictionaryDetailDTO addDictionaryDetail(Long dictId, DictionaryDetailDTO detailDTO) {
        // TODO 判断重复
        return null;
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
}
