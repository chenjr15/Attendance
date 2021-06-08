package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IDictionaryService;
import dev.chenjr.attendance.service.dto.DictionaryDTO;
import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.PageWrapper;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dictionaries")
@Tag(name = "数据字典项", description = "男女未知")
public class DictionaryController {
    @Autowired
    IDictionaryService dictionaryService;

    @PostMapping("")
    @Operation(description = "添加数据字典，其可以明细一起添加,传入的时候不要传`defaultCode`")
    public RestResponse<DictionaryDTO> addDictionary(@RequestBody @Validated DictionaryDTO dictionaryDTO) {
        DictionaryDTO created = dictionaryService.addDictionary(dictionaryDTO);
        return RestResponse.okWithData(created);
    }

    @GetMapping("")
    @Operation(description = "列出所有数据字典")
    public RestResponse<PageWrapper<DictionaryDTO>> listDictionary(
            @RequestParam(defaultValue = "1") long curPage, @RequestParam(defaultValue = "10") long pageSize
    ) {
        PageWrapper<DictionaryDTO> dictionaryPage = dictionaryService.listDictionary(curPage, pageSize);
        return RestResponse.okWithData(dictionaryPage);
    }

    @GetMapping("/{dictId}")
    @Operation(description = "显示数据字典信息")
    public RestResponse<DictionaryDTO> getDictionary(@PathVariable Long dictId) {
        DictionaryDTO dto = dictionaryService.getDictionary(dictId);
        if (dto == null) {
            throw HttpStatusException.notFound();
        }
        return RestResponse.okWithData(dto);
    }

    @PutMapping("/{dictId}")
    @Operation(description = "修改数据字典(**不修改明细**)，返回修改后的数据", hidden = true)
    public RestResponse<DictionaryDTO> modifyDictionary(
            @RequestBody @Validated DictionaryDTO dictionaryDTO,
            @PathVariable Long dictId
    ) {
        dictionaryDTO.setId(dictId);
        DictionaryDTO dto = dictionaryService.modifyDictionary(dictionaryDTO);
        return RestResponse.okWithData(dto);
    }

    @PutMapping("/{dictId}/{detailId}")
    @Operation(description = "修改数据字典__明细项__，返回修改后的__整个数据字典__信息,`body`中的明细`id`可以不填", hidden = true)
    public RestResponse<DictionaryDTO> modifyDictionaryDetail(
            @RequestBody @Validated DictionaryDetailDTO detailDTO,
            @PathVariable Long dictId,
            @PathVariable Long detailId
    ) {
        detailDTO.setId(detailId);
        DictionaryDTO dto = dictionaryService.modifyDictionaryDetail(dictId, detailDTO);
        return RestResponse.okWithData(dto);
    }

    @DeleteMapping("/{dictId}")
    @Operation(description = "删除字典类型", hidden = true)
    public RestResponse<?> deleteDictionary(@PathVariable Long dictId) {
        dictionaryService.deleteDictionary(dictId);
        return RestResponse.okWithMsg("Deleted!");
    }
}
