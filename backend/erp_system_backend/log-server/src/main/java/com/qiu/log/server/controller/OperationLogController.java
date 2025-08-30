package com.qiu.log.server.controller;

import com.qiu.common.server.model.result.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Qiu
 * @date 2025-08-24, 星期日 上午 08:49
 */
@RestController
@RequestMapping("/log")
public class OperationLogController {

    @GetMapping("/health")
    public ResponseBody<String> health() {
        return ResponseBody.success("Log service is running");
    }

    @GetMapping("/list")
    public ResponseBody<String> getOperationLogs() {
        // TODO: 实现日志查询逻辑
        return ResponseBody.success("操作日志查询功能待实现");
    }
}
