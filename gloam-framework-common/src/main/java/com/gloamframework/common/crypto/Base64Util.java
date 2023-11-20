package com.gloamframework.common.crypto;

import cn.hutool.core.util.ArrayUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

/**
 * BASE64工具类
 */
public class Base64Util {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";
    private static final int CONNECT_TIME_OUT = 5000;
    private static final int CAPACITY = 1024 * 4;

    /**
     * BASE64解密
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(byte[] key) {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);

        return md5.digest();

    }

    /**
     * SHA加密
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();

    }

    /**
     * 图片转base64字符串
     *
     * @param imgFile 图片路径
     */
    public static String imageToBase64Str(String imgFile) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        byte[] data = new byte[]{};
        try (SeekableByteChannel channel = Files.newByteChannel(Paths.get(imgFile))) {
            while (channel.read(buffer) != -1) {
                buffer.flip();
                data = ArrayUtil.addAll(data, buffer.array());
                buffer.clear();
            }
        } catch (IOException e) {
            throw new IOException("图片路径转BASE64失败", e);
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 图片转base64字符串
     */
    public static String imageStreamToBase64Str(InputStream inputStream) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        byte[] data = new byte[]{};
        try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            while (channel.read(buffer) != -1) {
                buffer.flip();
                data = ArrayUtil.addAll(data, buffer.array());
                buffer.clear();
            }
        } catch (IOException e) {
            throw new IOException("图片转BASE64失败", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);

    }

    public static String urlImageToBase64(String Url, boolean force) throws Exception {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(Url);
            conn = (HttpURLConnection) url.openConnection();
            if (force) {
                //权限限制时可用此方法
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            }
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            inputStream = conn.getInputStream();
            return imageStreamToBase64Str(inputStream);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
