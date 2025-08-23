package com.qiu.user.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ERP 系统")
                        .description("ERP 系统")
                        .version("1.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                        .contact(new Contact()
                                .name("秋玄")
                                .email("2833780890@qq.com")
                        )
                        .termsOfService("Welcome to ERP System")
                );
    }

}
