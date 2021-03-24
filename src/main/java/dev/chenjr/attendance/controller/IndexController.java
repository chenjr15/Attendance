package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.utils.RandomUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/randomString")
    public String randomString(@RequestParam(value = "length", defaultValue = "12") Integer length) {
        return RandomUtil.randomString(length);
    }

}
