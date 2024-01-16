package com.gloamframework.core.exception;

/**
 * 不支持的功能，用于抛出未实现的功能
 * @author 晓龙
 */
public class GloamNonSupportedFunction extends GloamRuntimeException {

    public GloamNonSupportedFunction() {
    }

    public GloamNonSupportedFunction(String message) {
        super(message);
    }

    public GloamNonSupportedFunction(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamNonSupportedFunction(Throwable cause) {
        super(cause);
    }

    public GloamNonSupportedFunction(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamNonSupportedFunction(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamNonSupportedFunction(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
