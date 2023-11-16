package com.gloamframework.common.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * 随机生成key
 *
 * @author 晓龙
 */
public class GloamKeyGenerator {

    public static String generateAES128KeyString() {
        try {
            return generateString("AES", 128);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES随机Key失败", e);
        }
    }

    public static SecretKey generateAES128Key() {
        try {
            return generate("AES", 128);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES随机Key失败", e);
        }
    }

    public static byte[] generateAES128KeyByte() {
        try {
            return generateByte("AES", 128);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES随机Key失败", e);
        }
    }

    public static String generateString(String algorithm, int digit) throws NoSuchAlgorithmException {
        return byteToHexString(generateByte(algorithm, digit));
    }

    public static byte[] generateByte(String algorithm, int digit) throws NoSuchAlgorithmException {
        return generate(algorithm, digit).getEncoded();
    }

    /**
     * 生成key
     *
     * @param algorithm 加密算法
     * @param digit     生成位数，要生成多少位，只需要修改这里即可128, 192或256
     */
    public static SecretKey generate(String algorithm, int digit) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(digit);
        return kg.generateKey();
    }

    /**
     * byte数组转化为16进制字符串
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String strHex = Integer.toHexString(aByte);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0").append(strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
}
