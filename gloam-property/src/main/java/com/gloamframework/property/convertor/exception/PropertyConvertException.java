package com.gloamframework.property.convertor.exception;

import com.gloamframework.common.error.GloamInternalException;

@SuppressWarnings("unused")
public class PropertyConvertException extends GloamInternalException {

    public PropertyConvertException() {
    }

    public PropertyConvertException(String message) {
        super(message);
    }

    public PropertyConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyConvertException(Throwable cause) {
        super(cause);
    }

    public PropertyConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PropertyConvertException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public PropertyConvertException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
