package com.framework.web.exception;

import com.gloamframework.core.exception.GloamRuntimeException;
import com.gloamframework.web.response.WebResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月01日 16:33
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GloamRuntimeException.class)
    public WebResult<?> ex(GloamRuntimeException e) {
        return WebResult.fail(e.getMessage());
    }
}
