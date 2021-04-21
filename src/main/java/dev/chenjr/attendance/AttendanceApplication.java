package dev.chenjr.attendance;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import dev.chenjr.attendance.config.security.DocInfo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("dev.chenjr.attendance.dao.mapper")
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 所有url生效
                registry.addMapping("/**")
                        .allowCredentials(true)
                        // 通配所有Origin
                        .allowedOriginPatterns("*")
                        // preflight 会过来问能不能用下面的头
                        .allowedHeaders("Authorization", "Origin", "content-type")
                        // preflight 会过来问能不能用下面的方法
                        .allowedMethods("GET", "POST", "HEAD", "DELETE", "PUT");
            }
        };
    }

    @Bean
    MybatisSqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        // 用默认的SqlSessionFactoryBean会报没有语句(无法自动生成sql)
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public OpenAPI customOpenAPI(@Autowired DocInfo docInfo) {
        String securitySchemeName = "JWT";
        Info info = new Info()
                .title(docInfo.getTitle())
                .version(docInfo.getVersion())
                .description(docInfo.getRepo().toString())
                .contact(docInfo.getContact());

        Components securitySchemes = new Components()
                .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Jwt token 认证")
                );
        docInfo.getServers().add("http://localhost:8080");
        return new OpenAPI()
                .servers(docInfo.getServers().stream().map(s -> new Server().url(s)).collect(Collectors.toList()))
                .components(securitySchemes)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .info(info);
    }
}
