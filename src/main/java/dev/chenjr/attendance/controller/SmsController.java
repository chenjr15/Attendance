package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.exception.CodeMismatch;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@Tag(name = "短信验证码", description = "发送验证码、校验验证码")
public class SmsController {
    @Autowired
    private ISmsService smsService;


    @PostMapping("/{type}/{phone}/{code}")
    @Operation(description = "校验短信验证码是否匹配")
    @ResponseBody
    public RestResponse<Boolean> checkSmsCode(@PathVariable String type, @PathVariable String phone, @PathVariable String code) {
        boolean ok = smsService.codeValid(phone, type, code);

        if (!ok) {
            throw new CodeMismatch("Code mismatch!");
        }
        return RestResponse.okWithMsg("Matched");
    }

    @GetMapping("/{type}/{phone}")
    @Operation(description = "发送短信验证码")
    @ResponseBody
    public RestResponse<?> sendSmsCode(@PathVariable String type, @PathVariable String phone) {
        smsService.sendCode(phone, type);
        return RestResponse.ok();
    }


}
