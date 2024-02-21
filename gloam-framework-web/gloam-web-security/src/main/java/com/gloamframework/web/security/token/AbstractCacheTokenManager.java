package com.gloamframework.web.security.token;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.exception.TokenAnalysisException;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import com.gloamframework.web.security.token.exception.TokenGenerateException;
import com.gloamframework.web.security.token.exception.TokenKickOffException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Objects;

/**
 * token缓存层
 *
 * @author 晓龙
 */
@Slf4j
public abstract class AbstractCacheTokenManager extends AbstractTokenManager {

    /**
     * token的缓冲信息
     */
    @Data
    @Accessors(chain = true)
    public static class TokenInfo implements Serializable {

        private static final long serialVersionUID = 7476025474416793857L;

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

    private final GloamSecurityCacheManager cacheManager;

    protected AbstractCacheTokenManager(TokenProperties tokenProperties, GloamSecurityCacheManager cacheManager) {
        super(tokenProperties);
        this.cacheManager = cacheManager;
    }

    @Override
    public void authenticate(String subject, Device device, String platform) {
        super.authenticate(subject, device, platform);
        this.tokenWrite2Cache(subject, device, platform);
    }

    private void tokenWrite2Cache(String subject, Device device, String platform) {
        // 获取票据
        HttpServletResponse response = WebContext.obtainResponse();
        if (response == null) {
            log.error("token写入时获取当前响应失败");
            throw new TokenGenerateException("token生成失败");
        }
        String tokenJSON = response.getHeader(tokenProperties.getHeader());
        Token token = JSON.parseObject(tokenJSON, Token.class);
        TokenInfo tokenInfo = new TokenInfo().setToken(token).setDevice(device).setValidCount(-1);
        cacheManager.getCache().put(this.generateCacheKey(subject, device, platform), tokenInfo, tokenProperties.getRefreshTokenExpire());
    }

    @Override
    public void checkAuthentication(Device device, String platform) {
        // 先检查token
        super.checkAuthentication(device, platform);
        // 获取到解析的token主题
        HttpServletRequest request = WebContext.obtainRequest();
        if (request == null) {
            log.error("获取当前请求未空");
            throw new TokenAuthenticateException("Token认证失败");
        }
        String subject = (String) TokenAttribute.TOKEN_SUBJECT.obtain(request);
        // 是否需要刷新
        boolean refresh = (boolean) TokenAttribute.TOKEN_REFRESH.obtain(request);
        if (refresh) {
            // 移除当前token
            this.revoke(subject, device, platform);
            // 重新生成token
            this.authenticate(subject, device, platform);
        }
        Token userToken = TokenAttribute.TOKEN.obtainToken(request);
        if (userToken == null) {
            log.error("获取请求携带token失败");
            throw new TokenAuthenticateException("Token认证失败");
        }
        // 获取缓存token
        TokenInfo tokenInfo = cacheManager.getCache().get(this.generateCacheKey(subject, device, platform), TokenInfo.class);
        if (tokenInfo == null) {
            log.error("token认证: 在缓存中没有查询到相应的token，是否已过期");
            throw new TokenAuthenticateException("无效token");
        }
        // 判断token内容
        if (!StrUtil.equals(userToken.getAccessToken(), tokenInfo.token.getAccessToken())) {
            throw new TokenAuthenticateException("登录用户不一致，请重新登录");
        }
        // 是否还在线
        if (tokenInfo.isKickOff()) {
            throw new TokenAuthenticateException("您已被踢下线");
        }

    }

    @Override
    public void revoke(String subject, Device device, String platform) {
        // 在缓存中移除token
        cacheManager.getCache().evict(this.generateCacheKey(subject, device, platform));
    }

    @Override
    public void kickOff(String subject, Device device, String platform) {
        String cacheKey = this.generateCacheKey(subject, device, platform);
        // 在缓存中修改token信息
        TokenInfo tokenInfo = cacheManager.getCache().get(cacheKey, TokenInfo.class);
        if (tokenInfo == null) {
            throw new TokenKickOffException("当前用户不在线");
        }
        tokenInfo.setKickOff(true);
        cacheManager.getCache().put(cacheKey, JSON.toJSONString(tokenInfo), tokenProperties.getAuthentication().getKickOffTime());
    }

    /**
     * 缓存key生成,Token类型:token主题:token:设备:nonce
     *
     * @param subject token主题
     * @param device  设备
     */
    private String generateCacheKey(String subject, Device device, String platform) {
        // 处理主题
        if (StrUtil.isBlank(subject)) {
            subject = "anonymous";
        }
        // 使用存在同端排斥
        String repel = "allow";
        if (!tokenProperties.isEnableDeviceRepel()) {
            // 获取当前token
            Token token = TokenAttribute.TOKEN.obtainToken(WebContext.obtainRequest());
            if (token == null) {
                log.error("生成缓存Key时获取token失败");
                throw new TokenAnalysisException("获取token失败");
            }
            repel = token.getAccessToken().substring(0, 6);
        }
        String key = "GLOAM_TOKEN:" + subject + ":" + repel;
        // 处理设备
        if (Objects.nonNull(device)) {
            key = key + ":" + device.name();
        }
        // 处理平台
        if (StrUtil.isNotBlank(platform)) {
            key = key + ":" + platform;
        }
        return key;
    }

}
