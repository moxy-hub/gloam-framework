package com.gloamframework.core.lang.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * 非法状态异常
 *
 * @author 晓龙
 */
public class GloamIllegalStateException extends GloamRuntimeException {

    public GloamIllegalStateException() {
    }

    public GloamIllegalStateException(String message) {
        super(message);
    }

    public GloamIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamIllegalStateException(Throwable cause) {
        super(cause);
    }

    public GloamIllegalStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamIllegalStateException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamIllegalStateException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
