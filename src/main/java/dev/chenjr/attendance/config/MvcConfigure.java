package dev.chenjr.attendance.config;

import dev.chenjr.attendance.service.impl.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置跨域
        // 所有url生效
        registry.addMapping("/**")
                .allowCredentials(true)
                // 通配所有Origin
                .allowedOriginPatterns("*")
                // preflight 会过来问能不能用下面的头
                .allowedHeaders("Authorization", "Origin", "content-type")
                // preflight 会过来问能不能用下面的方法
                .allowedMethods("GET", "POST", "HEAD", "DELETE", "PUT", "PATCH");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源
        registry.addResourceHandler(fileStorageService.getUrlPrefix() + "**")
                .addResourceLocations("file:" + fileStorageService.getStoragePath(), "classpath:/static/avatar/");
    }
}
