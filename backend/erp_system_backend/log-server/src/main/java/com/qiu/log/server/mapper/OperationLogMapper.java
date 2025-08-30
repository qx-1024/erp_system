package com.qiu.log.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiu.log.client.model.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Qiu
 * @createDate 2025-08-24 08:43:34
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}
