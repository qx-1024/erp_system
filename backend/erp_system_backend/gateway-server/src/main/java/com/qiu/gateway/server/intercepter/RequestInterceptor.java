package com.qiu.gateway.server.intercepter;

import com.qiu.common.server.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author HuangHaoBin
 * @date 2025-08-23, 星期六 下午 09:28
 */
@Slf4j
// 过滤器优先级，数字越小优先级越高
@Order(0)
@Component
public class RequestInterceptor implements GlobalFilter {

    static {
        log.info("请求拦截器已完成加载............");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("开始拦截请求...........");

        // 拦截请求前的逻辑
        String path = exchange.getRequest().getPath().value();
        String method = String.valueOf(exchange.getRequest().getMethod());
        log.info("请求路径: {}", path);
        log.info("请求方法: {}", method);
        log.info("请求参数: {}", exchange.getRequest().getQueryParams());

        // 添加自定义请求头
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(r -> r.header(CommonConstants.GATEWAY_HEADER, CommonConstants.GATEWAY_TOKEN))
                .build();

        // 继续处理请求
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 拦截响应后的逻辑
            log.info("响应状态码: {}", exchange.getResponse().getStatusCode());
        }));
    }
}
