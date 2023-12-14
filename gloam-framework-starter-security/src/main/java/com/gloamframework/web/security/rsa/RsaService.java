package com.gloamframework.web.security.rsa;

import com.gloamframework.common.crypto.RSAUtil;
import com.gloamframework.core.exception.GloamRuntimeException;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;

/**
 * Rsa密钥对服务
 */
@Slf4j
public class RsaService {

    private static final String RSA_KEY = "RSA_KEY:%s";
    private static final int EXPIRE_TIME = 24 * 60 * 60 * 1000;

    @Autowired
    private GloamSecurityCacheManager cacheManager;

    /**
     * 获取rsa公钥
     *
     * @param serviceCode 服务code
     */
    public String getPublicKey(String serviceCode) {
        // 获取密钥对
        return this.getRsaKeypair(serviceCode).getPubKey();
    }

    /**
     * 获取rsa私钥
     *
     * @param serviceCode serviceCode 服务code
     */
    public String getPrivateKey(String serviceCode) {
        // 获取密钥对
        return this.getRsaKeypair(serviceCode).getPriKey();
    }

    private RSAUtil.RsaKeypair getRsaKeypair(String serviceCode) {
        if (StringUtils.isBlank(serviceCode)) {
            throw new GloamRuntimeException("获取密钥对失败！serviceCode不可为null");
        }
        // 先在缓存中获取
        RSAUtil.RsaKeypair rsaKeypair = cacheManager.getCache().get(getServiceRSAKey(serviceCode), RSAUtil.RsaKeypair.class);
        if (rsaKeypair != null) {
            // 返回缓存的密钥对
            return rsaKeypair;
        }
        // 如果缓存获取失败，重新初始化
        return initRsaKeypair(serviceCode);
    }

    /**
     * 初始化密钥对，将密钥对加入redis缓存中
     */
    private RSAUtil.RsaKeypair initRsaKeypair(String serviceCode) {
        if (StringUtils.isBlank(serviceCode)) {
            throw new GloamRuntimeException("初始化密钥对失败！serviceCode不可为null");
        }
        try {
            RSAUtil.RsaKeypair rsaKeypair = RSAUtil.initKey();
            log.debug("init rsa keypair : {}", rsaKeypair);
            // 序列化密钥对
            String rsaKey = getServiceRSAKey(serviceCode);
            cacheManager.getCache().put(rsaKey, rsaKeypair, EXPIRE_TIME);
            log.debug("cache rsa : {}", rsaKey);
            return rsaKeypair;
        } catch (NoSuchAlgorithmException e) {
            log.error("初始化rsa keypair 失败", e);
            throw new GloamRuntimeException("初始化rsa keypair 失败:{}", e.getMessage());
        }
    }

    private String getServiceRSAKey(String serviceCode) {
        return String.format(RSA_KEY, serviceCode);
    }
}
