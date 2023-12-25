package com.gloamframework.cloud.sentinel.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.gloamframework.cloud.sentinel.properties.limit.LimitRuleProperties;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.view.GloamView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 集中处理Sentinel异常
 *
 * @author 晓龙
 */
@Slf4j
public class GloamSentinelExceptionResolver implements HandlerExceptionResolver, Ordered {

    private final LimitRuleProperties limitRuleProperties;

    public GloamSentinelExceptionResolver(LimitRuleProperties limitRuleProperties) {
        this.limitRuleProperties = limitRuleProperties;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Throwable cause = ex.getCause();
        // 处理限流异常
        if (FlowException.class.isAssignableFrom(cause.getClass()) || BlockException.class.isAssignableFrom(cause.getClass())) {
            log.error("IP:[{}] 请求资源:[{}:{}] 进行限流,返回信息:{}", WebContext.obtainIp(request), request.getMethod(), request.getRequestURI(), limitRuleProperties.getException());
            ModelAndView modelAndView = new ModelAndView(new GloamView(WebResult.refuse(limitRuleProperties.getException())));
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return modelAndView;
        }
        return null;
    }
}
