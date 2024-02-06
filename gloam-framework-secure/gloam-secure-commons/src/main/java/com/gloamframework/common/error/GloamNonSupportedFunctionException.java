package com.gloamframework.common.error;

/**
 * 不支持的功能，用于抛出未实现的功能
 *
 * @author 晓龙
 */
public class GloamNonSupportedFunctionException extends GloamInternalException {

    public GloamNonSupportedFunctionException() {
    }

    public GloamNonSupportedFunctionException(String message) {
        super(message);
    }

    public GloamNonSupportedFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamNonSupportedFunctionException(Throwable cause) {
        super(cause);
    }

    public GloamNonSupportedFunctionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamNonSupportedFunctionException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamNonSupportedFunctionException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
