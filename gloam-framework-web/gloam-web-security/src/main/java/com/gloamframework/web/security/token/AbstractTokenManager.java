package com.gloamframework.web.security.token;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.domain.UnauthorizedToken;
import com.gloamframework.web.security.token.exception.TokenAuthenticateException;
import com.gloamframework.web.security.token.exception.TokenExpiredException;
import com.gloamframework.web.security.token.exception.TokenGenerateException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * token 实现的抽象类
 *
 * @author 晓龙
 */
@Slf4j
public abstract class AbstractTokenManager implements TokenManager {

    protected final TokenProperties tokenProperties;

    protected AbstractTokenManager(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }


    @Override
    public void authenticate(String subject, Device device, String platform) {
        if (StrUtil.isBlank(subject)) {
            throw new TokenGenerateException("Token生成失败:没有指定token的主题");
        }
        Date currentTime = new Date();
        // 过期时间
        Date tokenExpirationTime = DateUtils.addSeconds(currentTime, (int) this.tokenProperties.getAccessTokenExpire());
        Date refreshTokenExpirationTime = DateUtils.addSeconds(currentTime, (int) this.tokenProperties.getRefreshTokenExpire());
        // 生成token
        String accessToken = this.generateToken(subject, tokenExpirationTime);
        String refreshToken = this.generateToken(subject, refreshTokenExpirationTime);
        // 实例化缓存信息
        Token token = new Token(accessToken, refreshToken, tokenExpirationTime);
        // 写入响应头
        HttpServletResponse response = WebContext.obtainResponse();
        HttpServletRequest request = WebContext.obtainRequest();
        if (response == null || request == null) {
            log.error("token写入时获取当前响应和请求失败");
            throw new TokenGenerateException("Token生成失败");
        }
        TokenAttribute.TOKEN.setAttributes(request, token);
        response.setHeader(tokenProperties.getHeader(), JSON.toJSONString(token));
    }

    @Override
    public void checkAuthentication(Device device, String platform) {
        HttpServletRequest request = WebContext.obtainRequest();
        // 请求携带的token
        Token token = TokenAttribute.TOKEN.obtainToken(request);
        if (token == null) {
            log.error("未在请求中检测到token");
            throw new TokenAuthenticateException("无效token");
        }
        if (UnauthorizedToken.class.isAssignableFrom(token.getClass())) {
            log.error("未认证token");
            throw new TokenAuthenticateException("未认证请求");
        }
        // 先检查过期时间
        Date currentTime = new Date();
        if (token.getExpiration().before(currentTime)) {
            log.error("无效token,原因：token已过期,过期时间{}:,当前时间:{}", token.getExpiration(), currentTime);
            throw new TokenExpiredException("token已过期");
        }
        // 开始检查accessToken
        TokenAttribute.TOKEN_REFRESH.setAttributes(request, false);
        String subject;
        try {
            subject = this.verifyToken(token.getAccessToken());
        } catch (TokenExpiredException tokenException) {
            log.warn("token不正确或已过期，开始检测refreshToken");
            try {
                subject = this.verifyToken(token.getRefreshToken());
                // 解析成功，可以刷新
                TokenAttribute.TOKEN_REFRESH.setAttributes(request, true);
            } catch (TokenExpiredException refreshTokenException) {
                log.warn("refreshToken不正确或已过期", refreshTokenException);
                throw new TokenExpiredException("token已过期");
            }
        }
        TokenAttribute.TOKEN_SUBJECT.setAttributes(request, subject);
    }

    /**
     * 生成token
     *
     * @param subject        主题
     * @param expirationTime 过期时间
     * @return token
     */
    protected abstract String generateToken(String subject, Date expirationTime);

    protected abstract String verifyToken(String accessToken);
}
