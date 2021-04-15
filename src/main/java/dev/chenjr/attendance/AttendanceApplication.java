package dev.chenjr.attendance;


import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import dev.chenjr.attendance.config.security.DocInfo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("dev.chenjr.attendance.dao.mapper")
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
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
        Info info = new Info().
                title(docInfo.getTitle())
                .version(docInfo.getVersion())
                .description(docInfo.getRepo().toString())
                .contact(docInfo.getContact());

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
