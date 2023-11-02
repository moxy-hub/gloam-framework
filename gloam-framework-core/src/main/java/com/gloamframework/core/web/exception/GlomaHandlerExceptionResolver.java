package com.gloamframework.core.web.exception;

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
        log.error("如果需要，请对异常进行统一处理，默认返回500", ex);
        ModelAndView modelAndView = new ModelAndView();
        GloamView gloamView = new GloamView(WebResult.refuse("服务器无法处理您的请求"));
        modelAndView.setViewName(gloamView.getBeanName());
        modelAndView.setView(gloamView);
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

}
