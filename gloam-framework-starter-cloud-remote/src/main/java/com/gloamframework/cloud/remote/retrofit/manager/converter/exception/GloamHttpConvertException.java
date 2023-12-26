package com.gloamframework.cloud.remote.retrofit.manager.converter.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * 转换异常
 *
 * @author 晓龙
 */
public class GloamHttpConvertException extends GloamRuntimeException {

    public GloamHttpConvertException() {
    }

    public GloamHttpConvertException(String message) {
        super(message);
    }

    public GloamHttpConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamHttpConvertException(Throwable cause) {
        super(cause);
    }

    public GloamHttpConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamHttpConvertException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamHttpConvertException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
