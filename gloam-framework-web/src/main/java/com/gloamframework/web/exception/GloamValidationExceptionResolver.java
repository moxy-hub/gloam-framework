package com.gloamframework.web.exception;

import cn.hutool.core.collection.CollectionUtil;
import com.gloamframework.web.context.WebContext;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.view.GloamView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author 晓龙
 */
@Slf4j
public class GloamValidationExceptionResolver implements HandlerExceptionResolver, Ordered {
    @Override
    public int getOrder() {
        // 最先执行，覆盖默认的处理
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = this.handlerBindException(ex);
        // 如果modelView为空，说明没有正确处理，那么继续匹配ConstraintViolationException异常
        if (modelAndView == null) {
            modelAndView = this.handlerConstraintViolationException(ex);
        }
        if (modelAndView == null) {
            modelAndView = this.handlerMethodArgumentNotValidException(ex);
        }
        // 处理日志
        if (modelAndView != null) {
            log.error("IP:[{}] 请求资源:[{}:{}] : 参数校验错误 --> {}", WebContext.obtainIp(request), request.getMethod(), request.getRequestURI(), modelAndView.getView());
        }
        return modelAndView;
    }

    private ModelAndView handlerBindException(Exception exception) {
        if (!BindException.class.isAssignableFrom(exception.getClass())) {
            return null;
        }
        BindException bindException = (BindException) exception;
        // 处理BindException验证错误
        List<ObjectError> allErrors = bindException.getAllErrors();
        if (CollectionUtil.isEmpty(allErrors)) {
            return new ModelAndView(new GloamView(WebResult.fail("参数校验错误:{}", bindException.getMessage())));
        }
        return new ModelAndView(new GloamView(WebResult.fail(allErrors.get(0).getDefaultMessage())));
    }

    private ModelAndView handlerConstraintViolationException(Exception exception) {
        if (!ConstraintViolationException.class.isAssignableFrom(exception.getClass())) {
            return null;
        }
        return new ModelAndView(new GloamView(WebResult.refuse(exception.getMessage())));
    }

    private ModelAndView handlerMethodArgumentNotValidException(Exception exception) {
        if (!MethodArgumentNotValidException.class.isAssignableFrom(exception.getClass())) {
            return null;
        }
        MethodArgumentNotValidException notValidException = (MethodArgumentNotValidException) exception;
        List<ObjectError> allErrors = notValidException.getBindingResult().getAllErrors();
        if (CollectionUtil.isEmpty(allErrors)) {
            return new ModelAndView(new GloamView(WebResult.fail("参数校验错误:{}", notValidException.getMessage())));
        }
        return new ModelAndView(new GloamView(WebResult.fail(allErrors.get(0).getDefaultMessage())));
    }
}
