package com.qiu.erp.exception;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:47
 * 基础异常类，所有自定义异常都应继承此类
 * 提供错误码和错误信息的基础支持
 */
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private final String code;

    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法（带原始异常）
     * @param code 错误码
     * @param message 错误信息
     * @param cause 原始异常
     */
    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    // getter方法
    public String getCode() {
        return code;
    }
}
