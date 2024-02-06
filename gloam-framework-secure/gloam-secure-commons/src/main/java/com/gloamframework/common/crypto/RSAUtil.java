package com.gloamframework.common.crypto;


import com.gloamframework.common.crypto.exception.DecryptException;
import com.gloamframework.common.crypto.exception.EncryptException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RSA公钥/私钥
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 */
public class RSAUtil extends Base64Util {

    @Data
    @NoArgsConstructor
    public static class RsaKeypair {
        private String pubKey;
        private String priKey;
        private String pemPubKey;
        private String pemPriKey;

        private RsaKeypair(String pubKey, String priKey) {
            this.pubKey = pubKey;
            this.priKey = priKey;
            this.pemPubKey = "-----BEGIN PUBLIC KEY-----" +
                    "\n" +
                    this.pubKey +
                    "\n" +
                    "-----END PUBLIC KEY-----";
            this.pemPriKey = "-----BEGIN PRIVATE KEY-----" +
                    "\n" +
                    this.priKey +
                    "\n" +
                    "-----END PRIVATE KEY-----";
        }
    }

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        return sign(data.getBytes(), privateKey);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }


    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(), publicKey, sign);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }


    /**
     * 解密<br>
     * 用私钥解密
     */
    public static String decryptByPrivateKey(String data, String key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, DecryptException {
        return new String(Objects.requireNonNull(decryptByPrivateKey(decryptBASE64(data), key)));
    }

    /**
     * 解密<br>
     * 用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, DecryptException {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        try (InputStream ins = new ByteArrayInputStream(data); ByteArrayOutputStream writer = new ByteArrayOutputStream()) {
            // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    System.arraycopy(buf, 0, block, 0, bufl);
                }
                writer.write(cipher.doFinal(block));
            }
            return writer.toByteArray();
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new DecryptException("解密失败", e);
        }
    }


    /**
     * 加密<br>
     * 用公钥加密
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        return encryptBASE64(encryptByPublicKey(data.getBytes(), key));
    }


    /**
     * 加密<br>
     * 用公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        return encrypt(data, keyFactory.getAlgorithm(), publicKey);
    }


    /**
     * 加密<br>
     * 用公钥加密
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        return encryptBASE64(encryptByPrivateKey(data.getBytes(), key));
    }


    /**
     * 加密<br>
     * 用私钥加密
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        return encrypt(data, keyFactory.getAlgorithm(), privateKey);
    }

    private static byte[] encrypt(byte[] data, String algorithm, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        try (ByteArrayOutputStream writer = new ByteArrayOutputStream(); InputStream ins = new ByteArrayInputStream(data)) {
            byte[] buf = new byte[117];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    System.arraycopy(buf, 0, block, 0, bufl);
                }
                writer.write(cipher.doFinal(block));
            }
            return writer.toByteArray();
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptException("加密失败", e);
        }
    }

    public static RsaKeypair initKey() throws NoSuchAlgorithmException {
        return initKey(1024);
    }

    /**
     * 初始化密钥
     */
    public static RsaKeypair initKey(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RsaKeypair(encryptBASE64(publicKey.getEncoded()),
                encryptBASE64(privateKey.getEncoded()));
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     */
    public static String replaceSpecialStr(String str) {

        String repl = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            repl = m.replaceAll("");
        }
        return repl;
    }

}
