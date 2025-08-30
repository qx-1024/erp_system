package com.qiu.log.server.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.common.server.utils.SnowflakeIdGenerator;
import com.qiu.log.server.service.OperationLogService;
import com.qiu.log.server.mapper.OperationLogMapper;
import com.qiu.log.client.model.OperationLog;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Qiu
 * @createDate 2025-08-24 08:43:34
 */
@Slf4j
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
        implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public boolean addOperationLog(OperationLog operationLog) {
        operationLog.setId(snowflakeIdGenerator.nextId());
        operationLog.setCreateTime(new Date());

        log.info("正在添加操作日志：用户[{}] 执行操作[{}]",
                operationLog.getUserName(), operationLog.getOperationContent());

        return this.save(operationLog);
    }

    @Override
    public List<OperationLog> getAllOperationLogs() {
        List<OperationLog> logs = new LambdaQueryChainWrapper<>(operationLogMapper)
                .eq(OperationLog::getIsDel, 0)
                .orderByDesc(OperationLog::getCreateTime)
                .list();
        log.info("查询到{}条操作日志", logs.size());
        return logs;
    }

    @Override
    public List<OperationLog> getOperationLogsByUserId(Long userId) {
        List<OperationLog> logs = new LambdaQueryChainWrapper<>(operationLogMapper)
                .eq(OperationLog::getIsDel, 0)
                .eq(OperationLog::getUserId, userId)
                .orderByDesc(OperationLog::getCreateTime)
                .list();
        log.info("查询到用户[{}]的{}条操作日志", userId, logs.size());
        return logs;
    }

    @Override
    public List<OperationLog> getOperationLogsByType(String operationType) {
        List<OperationLog> logs = new LambdaQueryChainWrapper<>(operationLogMapper)
                .eq(OperationLog::getIsDel, 0)
                .eq(OperationLog::getOperationType, operationType)
                .orderByDesc(OperationLog::getCreateTime)
                .list();
        log.info("查询到操作类型[{}]的{}条日志", operationType, logs.size());
        return logs;
    }
}
