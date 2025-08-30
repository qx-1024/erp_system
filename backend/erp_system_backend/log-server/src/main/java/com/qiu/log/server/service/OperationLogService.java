package com.qiu.log.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiu.log.client.model.OperationLog;

import java.util.List;

/**
 * @author Qiu
 * @createDate 2025-08-24 08:43:34
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 添加操作日志
     * 
     * @param operationLog 操作日志信息
     * @return 是否添加成功
     */
    boolean addOperationLog(OperationLog operationLog);

    /**
     * 获取所有操作日志
     * 
     * @return 操作日志列表
     */
    List<OperationLog> getAllOperationLogs();

    /**
     * 根据用户ID查询操作日志
     * 
     * @param userId 用户ID
     * @return 操作日志列表
     */
    List<OperationLog> getOperationLogsByUserId(Long userId);

    /**
     * 根据操作类型查询操作日志
     * 
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLog> getOperationLogsByType(String operationType);
}
