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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class DictionaryService implements IDictionaryService {
    @Autowired
    DictionaryMapper dictMapper;
    @Autowired
    DictionaryDetailMapper detailMapper;
    Map<String, Boolean> cacheNeedUpdate = new TreeMap<>();
    Map<String, Map<Integer, String>> dictCacheMap = new TreeMap<>();

    /**
     * 获取缓存数据
     *
     * @param key 字典的code
     * @return 字典map
     */
    @Override
    public Map<Integer, String> getCacheDict(String key) {
        if (cacheNeedUpdate.getOrDefault(key, true)) {
            // 更新
            DictionaryDTO dictionaryByCode = this.getDictionaryByCode(key);
            Map<Integer, String> detailMap = dictCacheMap.computeIfAbsent(key, k -> new TreeMap<>());
            for (DictionaryDetailDTO detail : dictionaryByCode.getDetails()) {
                detailMap.put(detail.getValue(), detail.getName());
            }
            cacheNeedUpdate.put(key, false);
        }
        return dictCacheMap.get(key);
    }

    /**
     * 获取缓存数据
     *
     * @param key       字典的code
     * @param detailKey 详情的值
     * @return 详情名字
     */
    @Override
    public String getCacheDictDetail(String key, int detailKey) {
        return this.getCacheDict(key).get(detailKey);
    }

    /**
     * 获取缓存数据
     *
     * @param key          字典的code
     * @param detailKey    详情的值
     * @param defaultValue 默认值
     * @return 详情名字
     */
    @Override
    public String getCacheDictDetail(String key, int detailKey, String defaultValue) {
        String cacheDictDetail = getCacheDictDetail(key, detailKey);
        if (cacheDictDetail == null) {
            cacheDictDetail = defaultValue;
        }
        return cacheDictDetail;
    }

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
        int defaultValue = createdDTO.getDefaultValue();

        boolean gotDefault = false;
        // 逐条创建明细
        List<DictionaryDetailDTO> details = dictionaryDTO.getDetails();
        for (int i = 0, detailsSize = details.size(); i < detailsSize; i++) {
            DictionaryDetailDTO detailDTO = details.get(i);
            detailDTO.setId(null);
            // 设置order
            detailDTO.setOrder(i);
            // 寻找default的detail
            if (defaultValue == detailDTO.getValue()) {
                detailDTO.setDefault(true);
                gotDefault = true;
            } else {
                detailDTO.setDefault(false);
            }
            final DictionaryDetailDTO createdDetailDTO = this.addDictionaryDetailNoCheck(createdDTO.getId(), detailDTO);
            createdDTO.getDetails().add(createdDetailDTO);
        }
        // 如果都没设置默认项(或者设置的值不在给定的选项中)的话就设置第一个为默认项
        if (!gotDefault && createdDTO.getDetails().size() != 0) {
            DictionaryDetailDTO detailDTO = createdDTO.getDetails().get(0);
            detailDTO.setDefault(true);
            detailMapper.setDefault(detailDTO.getId());
            createdDTO.setDefaultValue(defaultValue);
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
        Optional<Boolean> exists = this.dictMapper.exists(dictId);
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound();
        }
        // 防止传入id
        detailDTO.setId(null);
        return addDictionaryDetailNoCheck(dictId, detailDTO);
    }

    @Transactional
    public DictionaryDetailDTO addDictionaryDetailNoCheck(long dictId, DictionaryDetailDTO detailDTO) {
        DictionaryDetail dictionaryDetail = dto2Detail(dictId, detailDTO);
        if (dictionaryDetail.getDefaultItem()) {
            // 将原来的非默认属性改成默认属性，将原来的default设为False
            detailMapper.unSetDefault(dictId);
        }
        int insert = detailMapper.insert(dictionaryDetail);
        // TODO 处理插入失败
        return detailDTO;
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
        Page<Dictionary> page = new Page<>(curPage, pageSize);
        QueryWrapper<Dictionary> wr = new QueryWrapper<Dictionary>().select("id");
        dictMapper.selectPage(page, wr);
        PageWrapper<DictionaryDTO> pageWrapper = PageWrapper.fromIPage(page);
        List<Dictionary> records = page.getRecords();
        if (records != null && records.size() != 0) {
            List<DictionaryDTO> dictDtoList = new ArrayList<>(records.size());
            for (Dictionary dict : records) {
                DictionaryDTO dto = getDictionary(dict.getId());
                dictDtoList.add(dto);
            }
            pageWrapper.setContent(dictDtoList);
        }

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
        fillDetails(dto);
        return dto;
    }

    /**
     * 填充一个数据字典的详情项,和默认项
     *
     * @param dto 需要填充的数据字典
     */
    private void fillDetails(DictionaryDTO dto) {
        Stream<DictionaryDetailDTO> detailStream = getDictionaryDetailStream(dto.getId());
        List<DictionaryDetailDTO> detailDTOS = detailStream.collect(Collectors.toList());
        DictionaryDetailDTO defaultDetail = getDefaultDetail(detailDTOS.stream());
        if (defaultDetail != null) {
            dto.setDefaultValue(defaultDetail.getValue());
            dto.setDefaultName(defaultDetail.getName());
        }
        dto.setDetails(detailDTOS);
    }


    /**
     * 修改数据字典(不修改明细！)
     *
     * @param dictionaryDTO 修改的数据
     * @return 修改后的数据
     */
    @Override
    public DictionaryDTO modifyDictionary(DictionaryDTO dictionaryDTO) {
        this.cacheNeedUpdate.clear();
        Optional<Boolean> exists = dictMapper.exists(dictionaryDTO.getId());
        if (!exists.isPresent()) {
            throw HttpStatusException.notFound("Can not found dict by id:" + dictionaryDTO.getId().toString());
        }
        Dictionary dictionary = dto2Dict(dictionaryDTO);
        dictionary.updateBy(0L);
        this.dictMapper.updateById(dictionary);

        return this.getDictionary(dictionaryDTO.getId());
    }

    /**
     * 修改某个数据字典的某个明细项，如果设置了default会导致其他变更
     *
     * @param dictId     数据字典id
     * @param desiredDTO 要修改的明细
     * @return 修改后的完整的数据字典
     */
    @Override
    @Transactional
    public DictionaryDTO modifyDictionaryDetail(long dictId, DictionaryDetailDTO desiredDTO) {
        this.cacheNeedUpdate.clear();

        DictionaryDetail existingDetail = detailMapper.selectById(desiredDTO.getId());
        if (existingDetail == null) {
            throw HttpStatusException.notFound();
        }
        if (!existingDetail.getDefaultItem() && desiredDTO.isDefault()) {
            // 将原来的非默认属性改成默认属性，将原来的default设为False
            detailMapper.unSetDefault(dictId);
            detailMapper.setDefault(existingDetail.getId());
        }
        DictionaryDetail dictionaryDetail = dto2Detail(dictId, desiredDTO);
        dictionaryDetail.updateBy(0L);
        detailMapper.updateById(dictionaryDetail);
        return this.getDictionary(dictId);
    }

    /**
     * 删除指定的数据字典明细
     *
     * @param detailId 要删除的明细id
     */
    @Override
    public void deleteDictionaryDetail(long detailId) {

        this.cacheNeedUpdate.clear();
        this.detailMapper.deleteByDictId(detailId);
    }

    /**
     * 删除指定的数据字典
     *
     * @param dictId 要删除的数据字典id
     */
    @Override
    public void deleteDictionary(long dictId) {
        this.cacheNeedUpdate.clear();

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
        fillDetails(dto);
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
        return getDictionaryDetailStream(dictId).collect(Collectors.toList());
    }

    private Stream<DictionaryDetailDTO> getDictionaryDetailStream(long dictId) {
        List<DictionaryDetail> details = detailMapper.getByDictId(dictId);
        return details.stream().map(this::detail2DTO);
    }

    public DictionaryDetailDTO getDefaultDetail(Stream<DictionaryDetailDTO> stream) {
        Optional<DictionaryDetailDTO> defaultOpt = stream.filter(DictionaryDetailDTO::isDefault).findFirst();
        return defaultOpt.orElse(null);
    }


    private Dictionary dto2Dict(DictionaryDTO dictionaryDTO) {
        final Dictionary dictionary = new Dictionary();
        dictionary.setId(dictionaryDTO.getId());
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
        detailDTO.setDefault(detail.getDefaultItem());
        detailDTO.setHidden(detail.getHidden());
        detailDTO.setOrder(detail.getOrderValue());
        detailDTO.setCode(detail.getItemCode());
        return detailDTO;
    }

    private DictionaryDetail dto2Detail(Long dictId, DictionaryDetailDTO detailDTO) {
        DictionaryDetail dictionaryDetail = new DictionaryDetail();
        dictionaryDetail.setDictionaryId(dictId);
        dictionaryDetail.setHidden(detailDTO.isHidden());
        dictionaryDetail.setDefaultItem(detailDTO.isDefault());
        dictionaryDetail.setItemName(detailDTO.getName());
        dictionaryDetail.setItemValue(detailDTO.getValue());
        dictionaryDetail.setOrderValue(detailDTO.getOrder());
        dictionaryDetail.setId(detailDTO.getId());
        dictionaryDetail.setItemCode(detailDTO.getCode());

        return dictionaryDetail;
    }
}
