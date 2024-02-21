package com.gloamframework.web.security.token;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.gloamframework.web.security.GloamSecurityContext;
import com.gloamframework.web.security.annotation.Authentication;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.domain.UnauthorizedToken;
import com.gloamframework.web.security.token.exception.TokenAnalysisException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * token认证过滤器
 *
 * @author 晓龙
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class TokenAuthenticationFilter extends GloamOncePerRequestFilter {

    private final TokenManager tokenManager;
    private final TokenProperties tokenProperties;

    public TokenAuthenticationFilter(TokenManager tokenManager, TokenProperties tokenProperties) {
        this.tokenManager = tokenManager;
        this.tokenProperties = tokenProperties;
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 匹配认证策略
        Authentication.TokenStrategy strategy = (Authentication.TokenStrategy) TokenAttribute.TOKEN_STRATEGY.obtain(request);
        if (Objects.isNull(strategy)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (strategy.equals(Authentication.TokenStrategy.WANT)) {
            try {
                this.authentication(request);
            } catch (Exception exception) {
                // 尝试解析，不考虑成功率
                log.warn("try to authenticate token but found exception,because use [WANT] strategy,so this request pass without authentication,full exception", exception);
            }
        } else {
            // 进行认证
            this.authentication(request);
        }
        filterChain.doFilter(request, response);
    }

    private void authentication(HttpServletRequest request) {
        // 认证前处理请求携带的token
        this.tokenPreHandler(request);
        String platform = tokenProperties.getEnv().getPlatform();
        // 进行token认证
        tokenManager.checkAuthentication((Device) request.getAttribute(TokenAttribute.DEVICE.name()), platform);
        // 认证通过
        GloamSecurityContext.passAuthentication(TokenAttribute.TOKEN_SUBJECT.obtain(request));
    }

    private void tokenPreHandler(HttpServletRequest request) {
        // 获取请求头中token的存储
        String tokenHeader = request.getHeader(tokenProperties.getHeader());
        Token token;
        if (StrUtil.isBlank(tokenHeader)) {
            token = new UnauthorizedToken();
        } else {
            token = this.analysisToken(tokenHeader, request);
        }
        // 将解析的token放入请求属性
        TokenAttribute.TOKEN.setAttributes(request, token);
    }

    private Token analysisToken(String tokenHeader, HttpServletRequest request) {
        String nonceHeader = request.getHeader(tokenProperties.getNonceHeader());
        if (StrUtil.isBlank(nonceHeader)) {
            log.error("无效请求:{},获取请求头nonce为空", request.getRequestURI());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 解密token字符串,接收hex类型加密序列
        try {
            AES aes = new AES(tokenProperties.getRequestAesSecret().getBytes(UTF_8));
            tokenHeader = aes.decryptStr(tokenHeader);
        } catch (Exception exception) {
            log.error("无效请求:{},请求的token 解密失败,", request.getRequestURI(), exception);
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String[] tokens = tokenHeader.split(tokenProperties.getSplit());
        if (tokens.length != 3) {
            log.error("无效请求:{},请求的token长度不正确", request.getRequestURI());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 处理nonce
        if (!StrUtil.equals(tokens[0], nonceHeader)) {
            log.error("无效请求:{},请求的nonce不正确", request.getRequestURI());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 处理时间
        if (!NumberUtil.isNumber(tokens[1])) {
            log.error("无效请求:{},请求的timestamp不正确", request.getRequestURI());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        Date requestTime = new Date(Long.parseLong(tokens[1]));
        if ((System.currentTimeMillis() - requestTime.getTime()) > tokenProperties.getRequestValidTime()) {
            log.error("无效请求:{},请求的timestamp已过期,间隔:{}毫秒", request.getRequestURI(), System.currentTimeMillis() - requestTime.getTime());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 处理token本体
        try {
            return JSON.parseObject(tokens[2], Token.class);
        } catch (Exception exception) {
            log.error("无效请求:{},请求的token JSON解析失败,", request.getRequestURI(), exception);
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}
