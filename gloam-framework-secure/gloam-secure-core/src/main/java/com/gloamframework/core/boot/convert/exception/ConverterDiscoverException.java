package com.gloamframework.core.boot.convert.exception;

import com.gloamframework.common.error.GloamInternalException;

/**
 * @author 晓龙
 */
public class ConverterDiscoverException extends GloamInternalException {

    public ConverterDiscoverException() {
    }

    public ConverterDiscoverException(String message) {
        super(message);
    }

    public ConverterDiscoverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConverterDiscoverException(Throwable cause) {
        super(cause);
    }

    public ConverterDiscoverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConverterDiscoverException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public ConverterDiscoverException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
