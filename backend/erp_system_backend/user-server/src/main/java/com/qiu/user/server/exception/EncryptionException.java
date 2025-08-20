package com.qiu.user.server.exception;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:48
 */
public class EncryptionException extends BaseException {

    // 加密失败
    public static final String ENCRYPT_ERROR = "10001";
    // 解密失败
    public static final String DECRYPT_ERROR = "10002";
    // 密钥无效
    public static final String KEY_INVALID = "10003";
    // 加密数据无效
    public static final String DATA_INVALID = "10004";

    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    public EncryptionException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造方法（带原始异常）
     * @param code 错误码
     * @param message 错误信息
     * @param cause 原始异常
     */
    public EncryptionException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    // 便捷的静态工厂方法，简化异常创建
    public static EncryptionException encryptError(Throwable cause) {
        return new EncryptionException(ENCRYPT_ERROR, "数据加密失败", cause);
    }

    public static EncryptionException decryptError(Throwable cause) {
        return new EncryptionException(DECRYPT_ERROR, "数据解密失败", cause);
    }

    public static EncryptionException keyInvalid(String message) {
        return new EncryptionException(KEY_INVALID, message);
    }

    public static EncryptionException dataInvalid(String message) {
        return new EncryptionException(DATA_INVALID, message);
    }
}
