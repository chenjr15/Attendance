package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.DictionaryDTO;
import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;

/**
 * 字典项服务，字典项子项设定、排序、默认项设定等
 */
public interface IDictionaryService extends IService {
    /**
     * 添加新的数据字典和明细项
     *
     * @param dictionaryDTO 前端过来的数据字典dto
     */
    void addDictionary(DictionaryDTO dictionaryDTO);

    /**
     * 分页获取数据字典
     *
     * @param curPage  当前页,1开始
     * @param pageSize 页面大小
     * @return 数据和分页信息
     */
    PageWrapper<DictionaryDTO> listDictionary(long curPage, long pageSize);

    /**
     * 获取某个数据字典的详细信息，包括字典明细项
     *
     * @param dictId 数据字典id
     * @return 数据字典详细信息
     */
    DictionaryDTO getDictionaryInfo(Long dictId);

    /**
     * 修改数据字典(全量修改)
     *
     * @param dictId        数据字典id
     * @param dictionaryDTO 修改的数据
     * @return 修改后的数据
     */
    DictionaryDTO modifyDictionary(Long dictId, DictionaryDTO dictionaryDTO);

    /**
     * 修改某个数据字典的某个明细项
     *
     * @param dictId    数据字典id
     * @param detailDTO 要修改的明细
     * @return 修改后的完整的数据字典
     */
    DictionaryDTO modifyDictionaryDetail(Long dictId, DictionaryDetailDTO detailDTO);

    /**
     * 删除指定的数据字典
     *
     * @param dictId 要删除的数据字典id
     */
    void deleteDictionary(Long dictId);

}
