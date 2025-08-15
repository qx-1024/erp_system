package com.qiu.erp.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:13
 * 雪花算法工具类（生成64位纯数字唯一ID）
 * 结构：0（符号位） + 41位时间戳 + 10位机器ID + 12位序列号
 */
public class SnowflakeIdGenerator {

    // 起始时间戳（2025-08-15 00:00:00）
    private final long startTimestamp = 1755187200000L;

    // 机器ID位数（10位，支持1024台机器）
    private final long workerIdBits = 10L;
    // 序列号位数（12位，每毫秒最多4096个ID）
    private final long sequenceBits = 12L;

    // 机器ID最大值（2^10 - 1）
    private final long maxWorkerId = ~(-1L << workerIdBits);
    // 序列号最大值（2^12 - 1）
    private final long maxSequence = ~(-1L << sequenceBits);

    // 机器ID左移位数
    private final long workerIdShift = sequenceBits;
    // 时间戳左移位数
    private final long timestampLeftShift = sequenceBits + workerIdBits;

    // 当前机器ID
    private long workerId;
    // 序列号
    private long sequence = 0L;
    // 上一次生成ID的时间戳
    private long lastTimestamp = -1L;

    // 无参构造（供Spring实例化）
    public SnowflakeIdGenerator() {}

    // 设置机器ID（由Spring配置类调用）
    public void setWorkerId(long workerId) {
        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException(String.format("workerId必须在0到%d之间", maxWorkerId));
        }
        this.workerId = workerId;
    }

    // 生成唯一ID
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        // 处理时钟回退
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(String.format("系统时钟回退，拒绝生成ID。差值：%d毫秒", lastTimestamp - currentTimestamp));
        }

        // 同一毫秒内序列号自增
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒重置序列号（随机起始值）
            sequence = ThreadLocalRandom.current().nextLong(10);
        }

        lastTimestamp = currentTimestamp;

        // 组合ID
        return (currentTimestamp - startTimestamp) << timestampLeftShift
                | (workerId << workerIdShift)
                | sequence;
    }

    // 等待下一个毫秒
    private long waitUntilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
