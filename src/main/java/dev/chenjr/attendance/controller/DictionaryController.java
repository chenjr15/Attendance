package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.DictionaryDetailDTO;
import dev.chenjr.attendance.service.dto.InputDictionaryDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dictionaries")
@Tag(name = "字典项", description = "男女未知")
public class DictionaryController {
    @PostMapping("")
    @Operation(description = "添加字典类型")
    public RestResponse<?> addDictionary(@RequestBody @Validated InputDictionaryDTO dictionaryDTO) {
        return RestResponse.notImplemented();
    }

    @GetMapping("")
    @Operation(description = "列出所有字典类型")
    public RestResponse<List<DictionaryDetailDTO>> listDictionary(@RequestParam long curPage, @RequestParam(defaultValue = "10") long pageSize) {
        return RestResponse.okWithData(new ArrayList<>());
    }

    @GetMapping("/{dictId}")
    @Operation(description = "显示字典类型信息")
    public RestResponse<DictionaryDetailDTO> getDictionaryInfo(@PathVariable Long dictId) {
        return RestResponse.okWithData(new DictionaryDetailDTO());
    }

    @PutMapping("/{dictId}")
    @Operation(description = "修改字典类型")
    public RestResponse<?> modifyDictionary(@RequestBody @Validated InputDictionaryDTO dictionaryDTO, @PathVariable Long dictId) {
        return RestResponse.notImplemented();
    }

    @DeleteMapping("/{dictId}")
    @Operation(description = "删除字典类型")
    public RestResponse<?> delDictionary(@PathVariable Long dictId) {
        return RestResponse.notImplemented();
    }
}
