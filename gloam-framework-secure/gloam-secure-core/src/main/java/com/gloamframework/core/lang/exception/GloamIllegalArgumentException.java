package com.gloamframework.core.lang.exception;

import com.gloamframework.common.error.GloamInternalException;

/**
 * 非法参数异常
 *
 * @author 晓龙
 */
public class GloamIllegalArgumentException extends GloamInternalException {

    public GloamIllegalArgumentException() {
    }

    public GloamIllegalArgumentException(String message) {
        super(message);
    }

    public GloamIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public GloamIllegalArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamIllegalArgumentException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamIllegalArgumentException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
