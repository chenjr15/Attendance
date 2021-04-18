package dev.chenjr.attendance.config.security;

import io.swagger.v3.oas.models.info.Contact;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "doc")
@Data
public class DocInfo {
    @Autowired
    Repo repo;
    String title;
    String version;
    Contact contact;

    @Component
    @ConfigurationProperties(prefix = "doc.repo")
    @Data
    public static class Repo {
        
        String docs;
        String mobile;
        String frontend;
        String backend;

        @Override
        public String toString() {

            String format = "项目仓库<ul>" +
                    "<li><a href=\"%s\">文档</a></li>" +
                    "<li><a href=\"%s\">移动端</a></li>" +
                    "<li><a href=\"%s\">前端</a></li>" +
                    "<li><a href=\"%s\">后端</a></li>" +
                    "<ul>";
            return String.format(format, docs, mobile, frontend, backend);

        }
    }
}

