package dev.chenjr.attendance.service;

import dev.chenjr.attendance.service.dto.DictionaryDTO;
import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.PageSort;
import dev.chenjr.attendance.service.dto.PageWrapper;

import java.util.List;
import java.util.Map;

/**
 * 字典项服务，字典项子项设定、排序、默认项设定等
 */
public interface IDictionaryService extends IService {
    /**
     * 获取缓存数据
     *
     * @param key 字典的code
     * @return 字典map
     */
    Map<Integer, String> getCacheDict(String key);

    /**
     * 获取缓存数据
     *
     * @param key       字典的code
     * @param detailKey 详情的值
     * @return 详情名字
     */
    String getCacheDictDetail(String key, int detailKey);

    /**
     * 获取缓存数据
     *
     * @param key          字典的code
     * @param detailKey    详情的值
     * @param defaultValue 默认值
     * @return 详情名字
     */
    String getCacheDictDetail(String key, int detailKey, String defaultValue);

    /**
     * 添加新的数据字典和明细项
     *
     * @param dictionaryDTO 前端过来的数据字典dto
     * @return 添加后的数据
     */
    DictionaryDTO addDictionary(DictionaryDTO dictionaryDTO);

    /**
     * 分页获取数据字典
     *
     * @param pageSort 分页参数
     * @return 数据和分页信息
     */
    PageWrapper<DictionaryDTO> listDictionary(PageSort pageSort);

    /**
     * 获取某个数据字典的详细信息，包括字典明细项
     *
     * @param dictId 数据字典id
     * @return 数据字典详细信息
     */
    DictionaryDTO getDictionary(long dictId);

    /**
     * 修改数据字典(不修改明细！)
     *
     * @param dictionaryDTO 修改的数据
     * @return 修改后的数据
     */
    DictionaryDTO modifyDictionary(DictionaryDTO dictionaryDTO);

    /**
     * 删除指定的数据字典
     *
     * @param dictId 要删除的数据字典id
     */
    void deleteDictionary(long dictId);

    /**
     * 通过英文标识获取数据字典
     *
     * @param code 英文标识
     * @return 数据字典
     */
    DictionaryDTO getDictionaryByCode(String code);

    /**
     * 给某个数据字典添加明细
     *
     * @param dictId    数据字典id
     * @param detailDTO 需要添加的明细
     * @return 添加后的结果
     */
    DictionaryDetailDTO addDictionaryDetail(long dictId, DictionaryDetailDTO detailDTO);

    /**
     * 查询某个字典的 所有字典明细
     *
     * @param dictId 字典id
     * @return 明细列表
     */
    List<DictionaryDetailDTO> getDictionaryDetails(long dictId);

    /**
     * 修改某个数据字典的某个明细项, 如果设置了default会导致其他变更
     *
     * @param dictId    数据字典id
     * @param detailDTO 要修改的明细
     * @return 修改后的完整的数据字典
     */
    DictionaryDTO modifyDictionaryDetail(long dictId, DictionaryDetailDTO detailDTO);

    /**
     * 删除指定的数据字典明细
     *
     * @param detailId 要删除的明细id
     */
    void deleteDictionaryDetail(long detailId);

    /**
     * 对字典明细进行重排
     *
     * @param dictId 字典id
     * @param idList 排序的id
     * @return 排序后的顺序
     */
    List<String> reorder(long dictId, List<Long> idList);
}
