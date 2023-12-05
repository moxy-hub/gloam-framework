package com.gloamframework.web.security.token;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.common.crypto.AESUtil;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.token.constant.Attribute;
import com.gloamframework.web.security.token.domain.Token;
import com.gloamframework.web.security.token.domain.UnauthorizedToken;
import com.gloamframework.web.security.token.exception.TokenAnalysisException;
import com.gloamframework.web.security.token.properties.TokenProperties;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * token的请求头前置校验处理</br>
 * token格式：
 * <li>请求头中携带加密token字符串，请求头name和加密密钥可在{@link TokenProperties}中进行配置
 * <li>请求头中需要携带nonce随机字符，同一个请求多次发起，应携带相同的nonce来进行后端的去重请求
 * <li>token的格式为 nonce + {@link TokenProperties#getTokenSplit()} + 请求时间 + token的json格式字符串
 * <li>请求时间的有效期可在{@link TokenProperties}中进行配置
 *
 * @author 晓龙
 */
@Slf4j
public class TokenPreHandlerFilter extends GloamOncePerRequestFilter {

    private final TokenProperties tokenProperties;

    public TokenPreHandlerFilter(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中token的存储
        String tokenHeader = request.getHeader(tokenProperties.getTokenHeader());
        try {
            Token token;
            if (StrUtil.isBlank(tokenHeader)) {
                token = new UnauthorizedToken();
            } else {
                token = this.analysisToken(tokenHeader, request);
            }
            // 将解析的token放入请求属性
            Attribute.setAttributes(request, Attribute.TOKEN, token);
            filterChain.doFilter(request, response);
        } finally {
            Attribute.removeAttributes(request);
        }
    }

    private Token analysisToken(String tokenHeader, HttpServletRequest request) {
        String nonceHeader = request.getHeader(tokenProperties.getNonceHeader());
        if (StrUtil.isBlank(nonceHeader)) {
            log.error("无效请求:{},获取请求头nonce为空", request.getRequestURI());
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // 解密token字符串,接收hex类型加密序列
        try {
            AESUtil.AES aes = AESUtil.getAES(AESUtil.Algorithm.CBC, tokenProperties.getTokenAesSecret(), AESUtil.CODE_HEX);
            tokenHeader = aes.decrypt(tokenHeader);
        } catch (Exception exception) {
            log.error("无效请求:{},请求的token 解密失败,", request.getRequestURI(), exception);
            throw new TokenAnalysisException("请求token无效").setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String[] tokens = tokenHeader.split(tokenProperties.getTokenSplit());
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
        if ((System.currentTimeMillis() - requestTime.getTime()) > tokenProperties.getTokenValidTime()) {
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

    @Override
    public int getOrder() {
        return 2;
    }

}
