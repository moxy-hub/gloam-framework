package com.gloamframework.cloud.remote.retrofit.exception;

import com.gloamframework.common.error.GloamInternalException;

/**
 * @author 晓龙
 */
public class HttpException extends GloamInternalException {

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public HttpException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
