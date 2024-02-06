package com.gloamframework.cloud.remote.feign.exception;

import com.gloamframework.common.error.GloamInternalException;

/**
 * @author 晓龙
 */
public class FeignException extends GloamInternalException {

    public FeignException() {
    }

    public FeignException(String message) {
        super(message);
    }

    public FeignException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeignException(Throwable cause) {
        super(cause);
    }

    public FeignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FeignException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FeignException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
