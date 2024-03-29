package com.gloamframework.cloud.remote.feign.interceptor;

import com.gloamframework.cloud.remote.feign.exception.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign请求拦截器（设置请求头，传递请求参数）
 * <p>
 * 说明：服务间进行feign调用时，不会传递请求头信息。
 * 通过实现RequestInterceptor接口，完成对所有的Feign请求,传递请求头和请求参数。
 *
 * @author 晓龙
 */
@Slf4j
public class GloamRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new FeignException("获取当前请求的ServletRequestAttributes失败");
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        // 将请求头转发
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
        template.removeHeader("Content-Type");
        template.header("Content-Type", "application/json;charset=utf-8");
        log.debug("[Feign]: 请求 -> {} # {}", request.getMethod(), request.getRequestURL());

    }

}
