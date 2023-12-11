package com.gloamframework.web.exception;

import com.gloamframework.core.exception.GloamRuntimeException;
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
    @SuppressWarnings("all")
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("IP:[{}] 请求资源:[{}:{}] 发生异常,异常类型:{},异常原因:{}", WebContext.obtainIp(request), request.getMethod(), request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        String errorMessage;
        if (GloamRuntimeException.class.isAssignableFrom(ex.getClass())) {
            errorMessage = ((GloamRuntimeException) ex).getMessage();
        } else {
            log.warn("对于非Gloam框架的异常，统一返回500，如有特殊处理，请自行全局处理");
            errorMessage = "服务器无法处理您的请求";
        }
        ModelAndView modelAndView = new ModelAndView(new GloamView(WebResult.refuse(errorMessage)));
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

}
