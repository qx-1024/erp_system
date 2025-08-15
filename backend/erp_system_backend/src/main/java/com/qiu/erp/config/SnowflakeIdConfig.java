package com.qiu.erp.config;

import com.qiu.erp.utils.SnowflakeIdGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:25
 * 雪花算法配置类（从配置文件读取机器ID）
 */
@Configuration
@EnableConfigurationProperties(SnowflakeProperties.class)
public class SnowflakeIdConfig {

    /**
     * 注册雪花算法生成器为Spring Bean，从配置文件读取机器ID
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator(SnowflakeProperties properties) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator();
        // 设置配置文件中的机器ID
        generator.setWorkerId(properties.getWorkerId());
        return generator;
    }
}
