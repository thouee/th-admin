package me.th.share.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;

@Slf4j
class JasyptUtilsTest {

    private static final String SALT = "passW0rd";

    @Test
    void encrypt() {
        String plaintext = "Aa@123456";
        log.info("明文：{}", plaintext);
        String ciphertext = encrypt(plaintext);
        log.info("加密后密文：{}", ciphertext);
    }

    @Test
    void decrypt() {
        String ciphertext = "8JbSv/PH1qVE8gEC/l8lQxy5rp0sHX+K";
        log.info("密文：{}", ciphertext);
        String plaintext = decrypt(ciphertext);
        if (plaintext == null) {
            log.warn("加密失败");
        } else {
            log.info("解密后明文：{}", plaintext);
        }
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return String
     */
    public static String encrypt(String plaintext) {
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(SALT);
        return basicTextEncryptor.encrypt(plaintext);
    }


    /**
     * 解密
     *
     * @param ciphertext 密文
     * @return String
     */
    public static String decrypt(String ciphertext) {
        ciphertext = "ENC(" + ciphertext + ")";
        if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)) {
            BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
            basicTextEncryptor.setPassword(SALT);
            return PropertyValueEncryptionUtils.decrypt(ciphertext, basicTextEncryptor);
        }
        return null;
    }
}