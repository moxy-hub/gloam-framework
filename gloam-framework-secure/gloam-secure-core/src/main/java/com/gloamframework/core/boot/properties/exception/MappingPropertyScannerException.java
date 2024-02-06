package com.gloamframework.core.boot.properties.exception;


@SuppressWarnings("unused")
public class MappingPropertyScannerException extends MappingPropertyException {

    public MappingPropertyScannerException() {
    }

    public MappingPropertyScannerException(String message) {
        super(message);
    }

    public MappingPropertyScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingPropertyScannerException(Throwable cause) {
        super(cause);
    }

    public MappingPropertyScannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MappingPropertyScannerException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public MappingPropertyScannerException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
