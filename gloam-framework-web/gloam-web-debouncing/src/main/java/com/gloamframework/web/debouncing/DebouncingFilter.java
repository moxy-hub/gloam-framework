package com.gloamframework.web.debouncing;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.debouncing.annotation.Debouncing;
import com.gloamframework.web.debouncing.exception.DebouncingException;
import com.gloamframework.web.debouncing.wrapper.RepeatedlyRequestWrapper;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.GloamSecurityContext;
import com.gloamframework.web.security.filter.GloamOncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class DebouncingFilter extends GloamOncePerRequestFilter {

    private final static String DEBOUNCING_KEY = "debouncing:%s";
    private final DebouncingMatcher debouncingMatcher;
    private final GloamSecurityCacheManager cacheManager;
    private final MD5 md5;

    DebouncingFilter(DebouncingMatcher debouncingMatcher, GloamSecurityCacheManager cacheManager) {
        this.debouncingMatcher = debouncingMatcher;
        this.cacheManager = cacheManager;
        md5 = MD5.create();
    }

    @Override
    protected void doGloamFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Debouncing debouncing = debouncingMatcher.match(request);
        if (Objects.isNull(debouncing)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 创建可重复读取body的流
        RepeatedlyRequestWrapper requestWrapper = null;
        try {
            requestWrapper = new RepeatedlyRequestWrapper(request);
        } catch (IOException e) {
            throw new DebouncingException("[Debouncing]:创建可重复读请求Wrapper失败", e);
        }
        // 接口防抖，主要需要判断请求的一致性，这里使用MD5签名
        // 参数拼接：Uri+userId+requestParam｜body｜pathParam
        String sign = this.sign(requestWrapper, debouncing);
        Integer time = cacheManager.getCache().get(this.getCacheKey(sign), Integer.class);
        if (Objects.nonNull(time)) {
            log.warn("[debouncing]:接口重复提交，路径：{},请求方法：{}", requestWrapper.getRequestURI(), requestWrapper.getMethod());
            throw new DebouncingException(debouncing.message());
        }
        cacheManager.getCache().put(this.getCacheKey(sign), debouncing.interval(), debouncing.interval());
        filterChain.doFilter(requestWrapper, response);
    }

    @Override
    public int getOrder() {
        return 6;
    }

    private String sign(RepeatedlyRequestWrapper request, Debouncing debouncing) {
        // token
        String token = debouncing.tokenLimit() ? this.obtainToken() : "";
        // 读取body
        String body = request.getBodyString();
        // 读取param
        String params = JSON.toJSONString(request.getParameterMap());
        // 请求地址
        String url = request.getRequestURI() + "#" + request.getMethod();
        // 请求IP
        String ip = debouncing.enableIp() ? WebContext.obtainIp(request) : "";
        // 签名
        return md5.digestHex(token + body + params + url + ip);
    }

    private String obtainToken() {
        String token = GloamSecurityContext.obtainAuthenticationPrincipal();
        if (StringUtils.isBlank(token)) {
            throw new DebouncingException("无效的用户token");
        }
        return token;
    }

    private String getCacheKey(String sign) {
        return String.format(DEBOUNCING_KEY, sign);
    }

}
