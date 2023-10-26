package com.gloamframework.core.boot.env.exception;

import com.gloamframework.core.boot.exception.GloamRuntimeException;

public class EnvironmentInstanceException extends GloamRuntimeException {

    public EnvironmentInstanceException() {
    }

    public EnvironmentInstanceException(String message) {
        super(message);
    }

    public EnvironmentInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvironmentInstanceException(Throwable cause) {
        super(cause);
    }

    public EnvironmentInstanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EnvironmentInstanceException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public EnvironmentInstanceException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
