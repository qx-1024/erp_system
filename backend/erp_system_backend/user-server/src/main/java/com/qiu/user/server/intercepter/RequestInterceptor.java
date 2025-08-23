package com.qiu.user.server.intercepter;

import com.qiu.common.server.constants.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Qiu
 * @date 2025-08-23, 星期六 下午 11:28
 * 防止绕过Gateway直接访问服务的拦截器
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否包含Gateway请求头
        String token = request.getHeader(CommonConstants.GATEWAY_HEADER);
        if (token == null || !token.equals(CommonConstants.GATEWAY_TOKEN)) {
            log.warn("拒绝绕过Gateway的请求: {}", request.getRequestURI());
            log.error("尝试绕过gateway直接访问服务的请求IP：" + request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied: Direct access not allowed");
            return false;
        }
        return true;
    }
}
