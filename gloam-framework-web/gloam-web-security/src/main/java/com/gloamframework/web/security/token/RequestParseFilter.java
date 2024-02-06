package com.gloamframework.web.security.token;

import cn.hutool.http.useragent.UserAgent;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.annotation.Authentication;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.match.AuthenticationMatcher;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 对请求进行统一处理
 *
 * @author 晓龙
 * @date 2024年02月05日 14:11
 */
@Slf4j
public class RequestParseFilter extends GloamOncePerRequestFilter {

    @Autowired
    private AuthenticationMatcher authenticationMatcher;

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[Start]:处理请求:{} # {}", request.getMethod(), request.getRequestURL());
        Device device;
        try {
            UserAgent userAgent = WebContext.obtainUserAgent(request);
            device = Device.match(userAgent);
        } catch (Exception e) {
            device = Device.UNKNOWN;
        }
        // 设置请求设备
        TokenAttribute.DEVICE.setAttributes(request, device);
        // 处理token策略
        Authentication.TokenStrategy tokenStrategy = authenticationMatcher.matchTokenStrategy(request);
        if (Objects.nonNull(tokenStrategy)) {
            TokenAttribute.TOKEN_STRATEGY.setAttributes(request, tokenStrategy);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 这是过滤器开始的起一个环境，在最后移除全部属性
            TokenAttribute.clearAll(request);
        }
        log.info("[End]:请求:{} # {} : 处理结束", request.getMethod(), request.getRequestURL());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
