package com.qiu.common.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:26
 */
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeProperties {
    /**
     * 机器ID（0-1023，默认 1）
     * This field represents the unique identifier for the machine in the
     * distributed system.
     * The valid range is from 0 to 1023, with a default value of 1.
     */
    private long workerId = 1;

    /**
     * Getter method for workerId
     * 
     * @return the worker ID of the machine
     */
    public long getWorkerId() {
        return workerId;
    }

    /**
     * Setter method for workerId
     * 
     * @param workerId the worker ID to set (must be between 0-1023)
     */
    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
}