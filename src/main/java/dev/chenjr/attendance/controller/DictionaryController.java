package dev.chenjr.attendance.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictionaries")
@Tag(name = "数据字典")
public class DictionaryController {
    @Autowired
    IDictionaryService dictionaryService;

    @PostMapping("")
    @Operation(description = "添加数据字典，其可以明细一起添加 \n" +
            "- 不传`defaultValue`则默认值为第一个，明细里的 `default` 没用\n" +
            "- `order`按照顺序自动生成\n")
    public RestResponse<DictionaryDTO> addDictionary(@RequestBody @Validated DictionaryDTO dictionaryDTO) {
        DictionaryDTO created = dictionaryService.addDictionary(dictionaryDTO);
        return RestResponse.okWithData(created);
    }

    @PostMapping("/{dictId}")
    @Operation(description = "添加数据字典明细")
    public RestResponse<DictionaryDetailDTO> addDictionaryDetail(
            @RequestBody @Validated DictionaryDetailDTO detailDTO,
            @PathVariable Long dictId) {
        DictionaryDetailDTO added = dictionaryService.addDictionaryDetail(dictId, detailDTO);
        return RestResponse.okWithData(added);
    }

    @GetMapping("")
    @Operation(description = "列出所有数据字典")
    public RestResponse<PageWrapper<DictionaryDTO>> listDictionary(
            @ParameterObject PageSort pageSort
    ) {
        PageWrapper<DictionaryDTO> dictionaryPage = dictionaryService.listDictionary(pageSort);
        return RestResponse.okWithData(dictionaryPage);
    }

    @GetMapping("/{dictId}")
    @Operation(description = "显示数据字典信息")
    public RestResponse<DictionaryDTO> getDictionary(@PathVariable Long dictId) {
        DictionaryDTO dto = dictionaryService.getDictionary(dictId);
        if (dto == null) {
            throw HttpStatusException.notFound("Can not found dict by id:" + dictId.toString());
        }
        return RestResponse.okWithData(dto);
    }

    @GetMapping("/code/{dictCode}")
    @Operation(description = "通过编码获取数据字典信息\n" +
            "- `sex` 性别\n" +
            "- `semester` 学期\n"
    )
    public RestResponse<DictionaryDTO> getDictionaryByCode(@PathVariable String dictCode) {
        DictionaryDTO dto = dictionaryService.getDictionaryByCode(dictCode);
        return RestResponse.okWithData(dto);
    }

    @PatchMapping("/{dictId}")
    @Operation(description = "修改数据字典(**不修改明细**)，返回修改后的数据。\n")
    public RestResponse<DictionaryDTO> modifyDictionary(
            @RequestBody @Validated DictionaryDTO dictionaryDTO,
            @PathVariable Long dictId
    ) {
        dictionaryDTO.setId(dictId);
        DictionaryDTO dto = dictionaryService.modifyDictionary(dictionaryDTO);
        return RestResponse.okWithData(dto);
    }

    @PatchMapping("/{dictId}/{detailId}")
    @Operation(description = "修改数据字典__明细项__，返回修改后的__整个数据字典__信息,`body`中的明细`id`可以不填\n " +
            "- 支持修改该项为默认项，传入` {\"default\": true}`")
    public RestResponse<DictionaryDTO> modifyDictionaryDetail(
            @RequestBody @Validated DictionaryDetailDTO detailDTO,
            @PathVariable Long dictId,
            @PathVariable Long detailId
    ) {
        detailDTO.setId(detailId);
        DictionaryDTO dto = dictionaryService.modifyDictionaryDetail(dictId, detailDTO);
        return RestResponse.okWithData(dto);
    }

    @DeleteMapping("/{dictId}/{detailId}")
    @Operation(description = "删除字典明细")
    public RestResponse<?> deleteDetail(
            @PathVariable Long dictId,
            @PathVariable Long detailId
    ) {

        dictionaryService.deleteDictionaryDetail(detailId);
        return RestResponse.ok();
    }

    @DeleteMapping("/{dictId}")
    @Operation(description = "删除字典类型")
    public RestResponse<?> deleteDictionary(@PathVariable long dictId) {
        dictionaryService.deleteDictionary(dictId);
        return RestResponse.okWithMsg("Deleted!");
    }

    @PostMapping("/{dictId}/orders")
    @Operation(description = "排序字典明细")
    public RestResponse<List<String>> reorderDictionary(
            @PathVariable long dictId,
            @RequestBody @JsonSerialize(using = ToStringSerializer.class) List<Long> idList
    ) {
        List<String> ordered = dictionaryService.reorder(dictId, idList);
        return RestResponse.okWithData(ordered);
    }
}
