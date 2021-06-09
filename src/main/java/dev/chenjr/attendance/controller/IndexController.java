package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.utils.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SecurityRequirements
@Tag(name = "首页", description = "just for fun")
public class IndexController {

    @GetMapping("/")
    @Operation(description = "Hello world！")
    public RestResponse<String> index(@RequestParam(value = "name", defaultValue = "World") String name) {
        return RestResponse.okWithData(String.format("Hello %s!", name));
    }

    @GetMapping("/version")
    @Operation(description = "Api Version")
    public RestResponse<String> version() {
        return RestResponse.okWithData("alpha.0.1");
    }

    @GetMapping("/randomString")
    public RestResponse<String> randomString(@RequestParam(value = "length", defaultValue = "12") Integer length) {
        return RestResponse.okWithData(RandomUtil.randomString(length));
    }

    @GetMapping("/randomNumberString")
    public RestResponse<String> randomNumberString(@RequestParam(value = "length", defaultValue = "12") Integer length) {
        return RestResponse.okWithData(RandomUtil.randomNumberString(length));
    }

}
