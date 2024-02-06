package com.gloamframework.web.security.filter;

import com.alibaba.fastjson.JSON;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.exception.GloamSecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * gloam的一次请求过滤器，在认证前置注册，有内部顺序排列
 * 可以通过{@link #getOrder()}方法来指定适配器加载的顺序
 *
 * @author 晓龙
 */
@Slf4j
public abstract class GloamOncePerRequestFilter extends OncePerRequestFilter implements Ordered {

    private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 统一处理内部gloam异常
        try {
            this.doGloamFilter(request, response, filterChain);
        } catch (GloamSecurityException gloamSecurityException) {
            log.error("gloam security filter runtime exception", gloamSecurityException);
            response.setContentType(RESPONSE_CONTENT_TYPE);
            response.setStatus(gloamSecurityException.getResponseStatus());
            response.getWriter().write(JSON.toJSONString(WebResult.fail(gloamSecurityException.getMessage())));
        }
    }

    protected abstract void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
