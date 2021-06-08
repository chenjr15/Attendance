package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.mapper.DictionaryMapper;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.dto.DictionaryDTO;
import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService implements IDictionaryService {
    @Autowired
    DictionaryMapper dictMapper;

    /**
     * 添加新的数据字典和明细项
     *
     * @param dictionaryDTO 前端过来的数据字典dto
     */
    @Override
    public void addDictionary(DictionaryDTO dictionaryDTO) {

    }

    /**
     * 分页获取数据字典
     *
     * @param curPage  当前页,1开始
     * @param pageSize 页面大小
     * @return 数据和分页信息
     */
    @Override
    public PageWrapper<DictionaryDTO> listDictionary(long curPage, long pageSize) {
        return null;
    }

    /**
     * 获取某个数据字典的详细信息，包括字典明细项
     *
     * @param dictId 数据字典id
     * @return 数据字典详细信息
     */
    @Override
    public DictionaryDTO getDictionaryInfo(Long dictId) {
        return null;
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
}
