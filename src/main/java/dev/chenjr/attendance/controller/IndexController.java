package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.utils.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;


@RestController
@SecurityRequirements
@Tag(name = "首页", description = "just for fun")
public class IndexController {
    @Value("${doc.version}")
    String version;
    
    @GetMapping("/")
    @Operation(description = "Hello world！")
    public RestResponse<String> index(@RequestParam(value = "name", defaultValue = "World") String name) {
        return RestResponse.okWithData(String.format("Hello %s!", name));
    }
    
    
    @RequestMapping("/echo")
    @Operation(description = "Echo", hidden = true)
    public RestResponse<Map<String, Object>> echo(
            HttpServletRequest request
    ) {
        TreeMap<String, String> headerMap = new TreeMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        TreeMap<String, Object> data = new TreeMap<>();
        data.put("header", headerMap);
        BufferedReader reader = null;
        int len;
        StringBuilder builder = new StringBuilder();
        char[] buf = new char[1024];
        try {
            reader = request.getReader();
            while ((len = reader.read(buf)) != -1) {
                builder.append(buf, 0, len);
            }
            
        } catch (IOException ignored) {
        }
        data.put("body", builder.toString());
        data.put("parameter", request.getParameterMap());
        return RestResponse.errorWithData(HttpStatus.OK, request, "IP:" + request.getRemoteHost(), data);
    }
    
    
    @GetMapping("/version")
    @Operation(description = "Api Version")
    public RestResponse<Map<String, String>> version() {
        Map<String, String> map = new TreeMap<>();
        map.put("version", version);
        String cwd = Paths.get(".").toAbsolutePath().normalize().toString();
        map.put("cwd", cwd);
        
        return RestResponse.okWithData(map);
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
