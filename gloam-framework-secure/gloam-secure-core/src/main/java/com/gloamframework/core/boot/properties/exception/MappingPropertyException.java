package com.gloamframework.core.boot.properties.exception;

import com.gloamframework.common.error.GloamInternalException;

@SuppressWarnings("unused")
public class MappingPropertyException extends GloamInternalException {

    public MappingPropertyException() {
    }

    public MappingPropertyException(String message) {
        super(message);
    }

    public MappingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingPropertyException(Throwable cause) {
        super(cause);
    }

    public MappingPropertyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MappingPropertyException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public MappingPropertyException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
