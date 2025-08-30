package com.qiu.log.server.controller;

import com.qiu.common.server.model.result.ResponseBody;
import com.qiu.log.client.model.OperationLog;
import com.qiu.log.server.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Qiu
 * @date 2025-08-24, 星期日 上午 08:49
 */
@Slf4j
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
@Tag(name = "操作日志管理", description = "操作日志相关接口")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/list")
    @Operation(summary = "查询所有操作日志", description = "获取系统中所有的操作日志记录")
    public ResponseBody<List<OperationLog>> getOperationLogs() {
        return ResponseBody.success(operationLogService.getAllOperationLogs());
    }

    @PostMapping("/add")
    @Operation(summary = "新增操作日志", description = "向数据库中添加新的操作日志记录")
    public ResponseBody<String> addOperationLog(
            @Parameter(description = "操作日志信息", required = true)
            @RequestBody @Validated OperationLog operationLog) {
        return operationLogService.addOperationLog(operationLog) ? ResponseBody.success() : ResponseBody.failed("操作日志添加失败");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询操作日志", description = "获取指定用户的所有操作日志")
    public ResponseBody<List<OperationLog>> getOperationLogsByUserId(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        return ResponseBody.success(operationLogService.getOperationLogsByUserId(userId));
    }

    @GetMapping("/type/{operationType}")
    @Operation(summary = "根据操作类型查询日志", description = "获取指定操作类型的所有日志")
    public ResponseBody<List<OperationLog>> getOperationLogsByType(
            @Parameter(description = "操作类型", required = true) @PathVariable String operationType) {
        return ResponseBody.success(operationLogService.getOperationLogsByType(operationType));
    }
}
