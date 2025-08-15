package com.qiu.erp.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QiuXuan
 * @contact 2833780890@qq.com
 * @date 2025-07-25, 星期五 上午 09:51
 * @description: PDF 工具应用入口
 * @version: 1.0
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi pdfApi() {
        return GroupedOpenApi.builder()
                .group("ERP System")
                .pathsToMatch("/**")
                .build();
    }
}
