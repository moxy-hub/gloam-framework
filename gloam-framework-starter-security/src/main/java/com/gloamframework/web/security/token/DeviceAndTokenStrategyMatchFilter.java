package com.gloamframework.web.security.token;

import cn.hutool.http.useragent.UserAgent;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.security.annotation.Token;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.security.match.TokenMatcher;
import com.gloamframework.web.security.token.constant.Device;
import com.gloamframework.web.security.token.constant.TokenAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求设备映射过滤器
 *
 * @author 晓龙
 */
@Slf4j
public class DeviceAndTokenStrategyMatchFilter extends GloamOncePerRequestFilter {

    @Autowired
    private TokenMatcher tokenMatcher;

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("请求:{} # {} : 开始进行处理", request.getMethod(), request.getRequestURL());
        Device device;
        try {
            UserAgent userAgent = WebContext.obtainUserAgent(request);
            device = Device.match(userAgent);
        } catch (Exception e) {
            device = Device.UNKNOWN;
        }
        TokenAttribute.DEVICE.setAttributes(request, device);
        // 处理token策略
        Token.Strategy strategy = tokenMatcher.matchStrategy(request);
        TokenAttribute.TOKEN_STRATEGY.setAttributes(request, strategy);
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 这是过滤器开始的起一个环境，在最后移除全部属性
            TokenAttribute.clearAll(request);
        }
        log.info("请求:{} # {} : 处理结束", request.getMethod(), request.getRequestURL());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
