package com.qiu.log.client.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 操作日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName(value ="t_operation_log")
public class OperationLog implements Serializable {
    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作内容
     */
    private String operationContent;

    /**
     * 操作IP
     */
    private String operationIp;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 操作设备信息
     */
    private String deviceInfo;

    /**
     * 操作耗时(单位:毫秒)
     */
    private Integer operationDuration;

    /**
     * 请求方法
     */
    private Object requestMethod;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 操作状态(0:失败 1:成功)
     */
    private Integer operationStatus;

    /**
     * 错误信息(操作失败时)
     */
    private String errorInfo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date modifiedTime;

    /**
     * 创建人ID
     */
    private Long opCreateId;

    /**
     * 创建人姓名
     */
    private String opCreateName;

    /**
     * 修改人ID
     */
    private Long opModifiedId;

    /**
     * 修改人姓名
     */
    private String opModifiedName;

    /**
     * 逻辑删除(0:未删除 1:已删除)
     */
    @TableLogic
    private Integer isDel;

    @Serial
    private static final long serialVersionUID = 1L;
}