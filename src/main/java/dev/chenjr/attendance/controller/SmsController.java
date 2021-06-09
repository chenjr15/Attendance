package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.CodeMismatch;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.dto.SmsCodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sms")
@Tag(name = "短信验证码", description = "发送验证码、校验验证码")
public class SmsController {
    @Autowired
    private ISmsService smsService;


    @PostMapping("/{type}/{phone}/{code}")
    @Operation(description = "校验短信验证码是否匹配")
    public RestResponse<Boolean> checkSmsCode(@PathVariable String type, @PathVariable String phone, @PathVariable String code) {
        boolean ok = smsService.codeValid(phone, type, code);
        if (!ok) {
            throw new CodeMismatch("Code mismatch!");
        }
        return RestResponse.okWithMsg("Matched");
    }

    @SecurityRequirements
    @PostMapping("/{type}/{phone}")
    @Operation(description = "发送短信验证码", deprecated = true)
    public RestResponse<?> sendSmsCode(@PathVariable String type, @PathVariable String phone) {
        smsService.sendCode(phone, type);
        return RestResponse.ok();
    }

    @GetMapping("/{type}/{phone}")
    @Operation(description = "测试！ 获取短信验证码")
    public RestResponse<?> getSmsCode(@PathVariable String type, @PathVariable String phone) {
        String smsCodeTes = smsService.getSmsCodeTes(phone, type);
        return RestResponse.okWithData(smsCodeTes);
    }

    @SecurityRequirements
    @PostMapping("")
    @Operation(description = "发送短信验证码")
    public RestResponse<?> sendSmsCode(@Validated @RequestBody SmsCodeDTO smsCodeDTO) {
        smsService.sendCode(smsCodeDTO.phone, smsCodeDTO.type);
        return RestResponse.ok();
    }


}
