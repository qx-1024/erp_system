package com.qiu.erp.utils;

import com.qiu.erp.config.EncryptionConfig;
import com.qiu.erp.exception.EncryptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author HuangHaoBin
 * @date 2025-08-15, 星期五 下午 05:39
 */
@Slf4j
@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static String secretKey;

    @Autowired
    public void setSecretKey(EncryptionConfig config) {
        String key = config.getSecretKey();
        if (key == null || key.length() != 16) {
            // 使用自定义异常
            throw EncryptionException.keyInvalid("加密密钥必须是16位字符串");
        }
        EncryptionUtil.secretKey = key;
    }

    private static String encryptLogic(String content) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            // 捕获通用异常并转换为自定义异常
            throw EncryptionException.encryptError(e);
        }
    }

    /**
     * 通用加密方法（处理加密逻辑和异常）
     * @param content 待加密内容
     * @return 加密后的内容，加密失败返回null
     */
    public static String encrypt(String content) {
        if (content == null) {
            return null;
        }
        try {
            return encryptLogic(content);
        } catch (EncryptionException e) {
            log.error("内容加密失败 [错误码: {}]，内容: {}, 原因: {}",
                    e.getCode(), content, e.getMessage(), e);
            if (EncryptionException.KEY_INVALID.equals(e.getCode())) {
                log.warn("加密密钥配置错误，请联系管理员检查配置文件");
            } else if (EncryptionException.ENCRYPT_ERROR.equals(e.getCode())) {
                log.error("加密过程失败，请检查加密工具");
            }
            return null;
        } catch (Exception e) {
            log.error("内容加密时发生未知异常，内容: {}", content, e);
            return null;
        }
    }

    public static String decrypt(String encryptedContent) {
        try {
            // 验证加密内容格式
            if (encryptedContent == null || encryptedContent.isEmpty()) {
                throw EncryptionException.dataInvalid("加密内容不能为空");
            }

            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedContent));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw EncryptionException.dataInvalid("加密内容格式错误，不是有效字符串");
        } catch (Exception e) {
            throw EncryptionException.decryptError(e);
        }
    }
}