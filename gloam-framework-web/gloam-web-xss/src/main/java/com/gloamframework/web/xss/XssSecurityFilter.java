package com.gloamframework.web.xss;

import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import com.gloamframework.web.xss.wrapper.XssRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Xss攻击过滤器
 */
@Slf4j
public class XssSecurityFilter extends GloamOncePerRequestFilter {

    public XssSecurityFilter() {
        log.info("[WebXss]: 启动XSS攻击过滤器");
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //注入xss过滤器实例
        XssRequestWrapper requestWrapper = new XssRequestWrapper(request);
        //过滤
        filterChain.doFilter(requestWrapper, response);
    }

    @Override
    public int getOrder() {
        return 5;
    }

}
