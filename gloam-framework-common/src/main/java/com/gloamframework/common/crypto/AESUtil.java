package com.gloamframework.common.crypto;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class AESUtil {

    @AllArgsConstructor
    public enum Algorithm {
        ECB("AES/ECB/PKCS5Padding"),
        CBC("AES/CBC/PKCS5Padding");
        private final String value;
    }

    public static class AES {
        private static final String KEY_AES = "AES";
        private final int decryptCode;
        private final Cipher cipher;
        private final SecretKeySpec keySpec;
        private final IvParameterSpec ivParameterSpec;

        public AES(Algorithm algorithm, String secretKey, byte[] iv, int decryptCode) throws NoSuchPaddingException, NoSuchAlgorithmException {
            this.decryptCode = decryptCode;
            cipher = Cipher.getInstance(algorithm.value);
            keySpec = new SecretKeySpec(secretKey.getBytes(), KEY_AES);
            ivParameterSpec = new IvParameterSpec(iv);
        }

        /**
         * 加密
         *
         * @param data 需要加密的内容
         */
        public String encrypt(final String data) {
            return doAES(data, Cipher.ENCRYPT_MODE, true);

        }

        /**
         * 解密
         *
         * @param data 待解密内容
         */
        public String decrypt(final String data) {
            return doAES(data, Cipher.DECRYPT_MODE, true);

        }

        private String doAES(String data, int mode, boolean result2Hex) {
            try {
                boolean encrypt = mode == Cipher.ENCRYPT_MODE;
                byte[] content;
                //true 加密内容 false 解密内容
                if (encrypt) {
                    content = data.getBytes(StandardCharsets.UTF_8);
                } else if (decryptCode == CODE_HEX) {
                    content = Hex.decodeHex(data.toCharArray());
                } else {
                    content = Base64.decodeBase64(data);
                }
                cipher.init(mode, keySpec, ivParameterSpec);
                byte[] result = cipher.doFinal(content);
                if (encrypt) {
                    if (result2Hex) {
                        return Hex.encodeHexString(result);
                    } else {
                        return Base64.encodeBase64String(result);
                    }
                } else {
                    return new String(result, StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                throw new RuntimeException("AES加解密失败", e);
            }
        }
    }


    public static final int CODE_BASE64 = 1;
    public static final int CODE_HEX = 2;

    private static final String iv = "E08ADE2699714B87";

    public static AES getAES(Algorithm algorithm, String secretKey) {
        return getAES(algorithm, secretKey, iv.getBytes(), CODE_BASE64);
    }

    public static AES getAES(Algorithm algorithm, String secretKey, byte[] iv, int decryptCode) {
        try {
            return new AES(algorithm, secretKey, iv, decryptCode);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("实例化AES失败", e);
        }
    }


}
