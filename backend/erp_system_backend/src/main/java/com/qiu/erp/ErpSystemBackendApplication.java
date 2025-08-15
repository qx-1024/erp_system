package com.qiu.erp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Qiu
 * @deprecated 进销存系统
 * @date 2025/08/15 15:50
 */
@SpringBootApplication(scanBasePackages = {"com.qiu.erp"})
@MapperScan(basePackages = "com.qiu.erp.mapper")
public class ErpSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErpSystemBackendApplication.class, args);
    }

}
