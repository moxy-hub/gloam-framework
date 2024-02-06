package com.gloamframework.core.boot.properties.exception;

@SuppressWarnings("unused")
public class MappingPropertyInstanceException extends MappingPropertyException {

    public MappingPropertyInstanceException() {
    }

    public MappingPropertyInstanceException(String message) {
        super(message);
    }

    public MappingPropertyInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingPropertyInstanceException(Throwable cause) {
        super(cause);
    }

    public MappingPropertyInstanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MappingPropertyInstanceException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public MappingPropertyInstanceException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
