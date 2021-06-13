package dev.chenjr.attendance;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import dev.chenjr.attendance.config.DocInfo;
import dev.chenjr.attendance.dao.enums.ParamEnum;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("dev.chenjr.attendance.dao.mapper")
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }


    @SuppressWarnings("deprecation")
    @Bean
    MybatisSqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) throws Exception {
        // 用默认的SqlSessionFactoryBean会报没有语句(无法自动生成sql)
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 加入分页的终极配置，PaginationInnerInterceptor 没用
        sqlSessionFactoryBean.setPlugins(new PaginationInterceptor());
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactoryBean.getObject().getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(ParamEnum.class, EnumOrdinalTypeHandler.class);
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
