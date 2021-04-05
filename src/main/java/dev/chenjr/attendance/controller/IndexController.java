package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.utils.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexController {

    @GetMapping("/")
    @Operation(description = "Hello world！")
    @ResponseBody
    public RestResponse<String> index(@RequestParam(value = "name", defaultValue = "World") String name) {
        return RestResponse.okWithData(String.format("Hello %s!", name));
    }

    @GetMapping("/randomString")
    @ResponseBody
    public RestResponse<String> randomString(@RequestParam(value = "length", defaultValue = "12") Integer length) {
        return RestResponse.okWithData(RandomUtil.randomString(length));
    }

}
