package com.qiu.erp.utils;

import com.qiu.erp.config.EncryptionConfig;
import com.qiu.erp.exception.EncryptionException;
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

    public static String encrypt(String content) {
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
            throw EncryptionException.dataInvalid("加密内容格式错误，不是有效的Base64字符串");
        } catch (Exception e) {
            throw EncryptionException.decryptError(e);
        }
    }
}