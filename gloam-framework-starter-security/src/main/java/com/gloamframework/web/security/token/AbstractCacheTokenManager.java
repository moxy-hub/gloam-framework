package com.gloamframework.web.security.token;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.data.redis.RedisUtil;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import com.gloamframework.web.security.token.exception.TokenGenerateException;
import com.gloamframework.web.security.token.exception.TokenKickOffException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * token缓存层
 *
 * @author 晓龙
 */
@Slf4j
public abstract class AbstractCacheTokenManager extends AbstractTokenManager {

    private enum TokenPrefix {
        TOKEN,
        TICKET;
    }

    /**
     * token的缓冲信息
     */
    @Data
    @Accessors(chain = true)
    private final static class TokenInfo {
        /**
         * token主体信息
         */
        private Token token;

        /**
         * token有效次数
         */
        private int validCount;

        /**
         * token设备
         */
        private Device device;

        /**
         * token是否被踢出
         */
        private boolean kickOff = false;
    }

    private final TokenProperties tokenProperties;
    private final GloamSecurityCacheManager cacheManager;

    protected AbstractCacheTokenManager(TokenProperties tokenProperties, GloamSecurityCacheManager cacheManager) {
        super(tokenProperties);
        this.tokenProperties = tokenProperties;
        this.cacheManager = cacheManager;
    }

    /**
     * 将生成的票据放在缓存中
     */
    @Override
    public void ticket(String subject, Device device) {
        // 先生成票据
        super.ticket(subject, device);
        this.tokenWrite2Cache(subject, device, TokenPrefix.TICKET);
    }

    @Override
    public void authenticate(String subject, Device device) {
        super.authenticate(subject, device);
        this.tokenWrite2Cache(subject, device, TokenPrefix.TOKEN);
    }

    private void tokenWrite2Cache(String subject, Device device, TokenPrefix tokenPrefix) {
        // 获取票据
        HttpServletResponse response = WebContext.obtainResponse();
        if (response == null) {
            log.error("token写入时获取当前响应失败");
            throw new TokenGenerateException("票据生成失败");
        }
        switch (tokenPrefix) {
            case TOKEN: {
                String tokenJSON = response.getHeader(tokenProperties.getHeader());
                Token token = JSON.parseObject(tokenJSON, Token.class);
                TokenInfo tokenInfo = new TokenInfo().setToken(token).setDevice(device).setValidCount(-1);
                cacheManager.getCache().put(this.generateCacheKey(tokenPrefix, subject, device), tokenInfo, tokenProperties.getRefreshTokenExpire());
                break;
            }
            case TICKET: {
                String ticketJSON = response.getHeader(tokenProperties.getTicket().getHeader());
                Token ticket = JSON.parseObject(ticketJSON, Token.class);
                TokenInfo ticketInfo = new TokenInfo().setToken(ticket).setDevice(device).setValidCount(tokenProperties.getTicket().getValidCount());
                cacheManager.getCache().put(this.generateCacheKey(tokenPrefix, subject, device), ticketInfo, tokenProperties.getTicket().getValidTime());
                break;
            }
            default: {
                log.warn("token只支持ticket和token两种方式");
                throw new TokenGenerateException("未知token类型");
            }
        }
    }

    @Override
    public boolean checkAuthentication(Device device) {
        // 先检查token
        if (!super.checkAuthentication(device)) {
            return false;
        }
        // 获取到解析的token主题
        HttpServletRequest request = WebContext.obtainRequest();
        if (request == null) {
            log.error("获取当前请求未空");
            throw new TokenAuthenticateException("Token认证失败");
        }
        String subject = (String) TokenAttribute.TOKEN_SUBJECT.obtain(request);
        // 分布式锁定token
        String randomLock = String.valueOf(System.currentTimeMillis() + RandomUtil.randomChar());
        try {
            if (!RedisUtil.LockOps.getLock(subject, randomLock, 5, TimeUnit.SECONDS)) {
                log.error("获取分布式锁失败，key:{},value:{}", subject, randomLock);
                // 获取锁失败
                throw new TokenAuthenticateException("同时有多人对该账号进行认证，请稍后再试，如果不是本人操作，请及时检查");
            }
            // 获取缓存token
            TokenInfo tokenInfo = cacheManager.getCache().get(this.generateCacheKey(TokenPrefix.TOKEN, subject, device), TokenInfo.class);
            if (tokenInfo == null) {
                log.error("token认证: 在缓存中没有查询到相应的token，是否已过期");
                throw new TokenAuthenticateException("无效token");
            }
            // 是否还在线
            if (tokenInfo.isKickOff()) {
                throw new TokenAuthenticateException("您已被踢下线");
            }
            // 是否需要刷新
            boolean refresh = (boolean) TokenAttribute.TOKEN_REFRESH.obtain(request);
            if (refresh) {
                // 移除当前token
                this.revoke(subject, device);
                // 重新生成token
                this.authenticate(subject, device);
            }
            return true;
        } finally {
            // 释放锁
            if (!RedisUtil.LockOps.releaseLock(subject, randomLock)) {
                log.error("释放分布式锁失败，key:{},value:{}", subject, randomLock);
            }
        }
    }

    @Override
    public void revoke(String subject, Device device) {
        // 在缓存中移除token
        cacheManager.getCache().evict(this.generateCacheKey(TokenPrefix.TOKEN, subject, device));
    }

    @Override
    public void kickOff(String subject, Device device) {
        // 在缓存中修改token信息
        TokenInfo tokenInfo = cacheManager.getCache().get(this.generateCacheKey(TokenPrefix.TOKEN, subject, device), TokenInfo.class);
        if (tokenInfo == null) {
            throw new TokenKickOffException("当前用户不在线");
        }
        tokenInfo.setKickOff(true);
        cacheManager.getCache().put(this.generateCacheKey(TokenPrefix.TOKEN, subject, device), JSON.toJSONString(tokenInfo), tokenProperties.getAuthentication().getKickOffTime());
    }

    /**
     * 缓存key生成,Token类型:token主题:token:设备:nonce
     *
     * @param prefix  token前缀
     * @param subject token主题
     * @param device  设备
     */
    private String generateCacheKey(TokenPrefix prefix, String subject, Device device) {
        // 处理设备
        String deviceValue;
        if (device != null) {
            deviceValue = device.name();
        } else {
            deviceValue = "all";
        }
        // 处理前缀
        if (prefix == null) {
            // 默认为token
            prefix = TokenPrefix.TOKEN;
        }
        // 处理主题
        if (StrUtil.isBlank(subject)) {
            subject = "anonymous";
        }
        // 使用存在同端排斥
        String repel = "allow";
        if (!tokenProperties.isEnableDeviceRepel()) {
            repel = System.currentTimeMillis() + RandomUtil.randomString(5);
        }
        return prefix + ":" + subject + ":" + deviceValue + ":" + repel;
    }

}
