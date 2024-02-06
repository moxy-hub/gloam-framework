package com.gloamframework.web.exception;

import com.gloamframework.common.error.ErrorCode;
import com.gloamframework.common.error.GloamBusinessException;
import com.gloamframework.common.error.GloamInternalException;
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

    /**
     * 对于异常后端全部返回的为200，但是在内部的状态码为500，如果前端接收到500的请求，说明后端没有处理异常
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the executed handler, or {@code null} if none chosen at the
     *                 time of the exception (for example, if multipart resolution failed)
     * @param ex       the exception that got thrown during handler execution
     */
    @Override
    @SuppressWarnings("all")
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("IP:[{}] 请求资源:[{}:{}] 发生异常,异常类型:{},异常原因:{}", WebContext.obtainIp(request), request.getMethod(), request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        ModelAndView modelAndView;
        if (GloamInternalException.class.isAssignableFrom(ex.getClass())) {
            // 对于内部错误，直接转换输出
            modelAndView = new ModelAndView(new GloamView(WebResult.refuse(((GloamInternalException) ex).getMessage())));
        } else if (GloamBusinessException.class.isAssignableFrom(ex.getClass())) {
            // todo 对于支持ErrorCode的错误码，进行国际化的翻译
            ErrorCode errorCode = ((GloamBusinessException) ex).getErrorCode();
            modelAndView = new ModelAndView(new GloamView(WebResult.refuse(((errorCode.getMessage())))));
        } else {
            log.warn("对于非Gloam框架的异常，统一返回500，如有特殊处理，请自行全局处理");
            modelAndView = new ModelAndView(new GloamView(WebResult.refuse("服务器无法处理您的请求")));
            // 对于没有处理的异常，服务器返回500
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelAndView;
    }

}
