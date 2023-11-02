package com.gloamframework.core.web.exception;

import com.gloamframework.core.web.context.WebContext;
import com.gloamframework.core.web.response.WebResult;
import com.gloamframework.core.web.view.GloamView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理服务器处理不了的异常
 *
 * @author 晓龙
 */
@Slf4j
public class GlomaHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("IP:[{}] 请求资源:[{}:{}] 发生异常,异常类型:{},异常原因:{}", WebContext.obtainIp(request), request.getMethod(), request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        log.warn("如果需要自定义异常返回,请对异常:{} 进行统一处理,默认返回500", ex.getClass());
        ModelAndView modelAndView = new ModelAndView(new GloamView(WebResult.refuse("服务器无法处理您的请求")));
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

}
