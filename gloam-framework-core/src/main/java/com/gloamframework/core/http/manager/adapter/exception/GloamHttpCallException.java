package com.gloamframework.core.http.manager.adapter.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * @author 晓龙
 */
public class GloamHttpCallException extends GloamRuntimeException {

    public GloamHttpCallException() {
    }

    public GloamHttpCallException(String message) {
        super(message);
    }

    public GloamHttpCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamHttpCallException(Throwable cause) {
        super(cause);
    }

    public GloamHttpCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamHttpCallException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamHttpCallException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
