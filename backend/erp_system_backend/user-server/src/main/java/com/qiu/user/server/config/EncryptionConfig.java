package com.qiu.user.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:39
 * @description 加密配置类（从配置文件读取密钥）
 */
@Configuration
@ConfigurationProperties(prefix = "encryption")
public class EncryptionConfig {

    /**
     * AES密钥（16位，AES-128）
     */
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}

