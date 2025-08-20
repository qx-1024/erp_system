package com.qiu.user.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Qiu
 */
@SpringBootApplication(scanBasePackages = {"com.qiu.user"})
@MapperScan(basePackages = "com.qiu.user.server.mapper")
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
