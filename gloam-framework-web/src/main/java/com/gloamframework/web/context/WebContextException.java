package com.gloamframework.web.context;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * web上下文异常
 *
 * @author 晓龙
 */
public class WebContextException extends GloamRuntimeException {

    public WebContextException() {
    }

    public WebContextException(String message) {
        super(message);
    }

    public WebContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebContextException(Throwable cause) {
        super(cause);
    }

    public WebContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WebContextException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public WebContextException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
