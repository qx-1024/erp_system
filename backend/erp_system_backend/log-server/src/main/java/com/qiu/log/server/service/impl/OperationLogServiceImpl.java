package com.qiu.log.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiu.log.server.service.OperationLogService;
import com.qiu.log.server.mapper.OperationLogMapper;
import com.qiu.log.client.model.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author Qiu
* @createDate 2025-08-24 08:43:34
*/
@Slf4j
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService{

}




