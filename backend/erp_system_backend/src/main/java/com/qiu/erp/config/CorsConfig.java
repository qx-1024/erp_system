package com.qiu.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author HuangHaoBin
 * @date 2025-08-16, 星期六 下午 10:16
 */
@Configuration
public class CorsConfig {

    /**
     * 配置跨域过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许的源（域名），*表示允许所有源
        config.addAllowedOriginPattern("*");

        // 允许的请求头
        config.addAllowedHeader("*");

        // 允许的请求方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");

        // 是否允许携带Cookie
        config.setAllowCredentials(true);

        // 预检请求的有效期（秒），即在这个时间内不需要再发送预检请求
        config.setMaxAge(3600L);

        // 创建路径匹配器
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 对所有路径应用跨域配置
        source.registerCorsConfiguration("/**", config);

        // 返回跨域过滤器
        return new CorsFilter(source);
    }

}
