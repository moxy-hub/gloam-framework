package com.gloamframework.core.boot.env.exception;

import com.gloamframework.core.boot.exception.GloamRuntimeException;

public class EnvironmentConfigException extends GloamRuntimeException {

    public EnvironmentConfigException() {
    }

    public EnvironmentConfigException(String message) {
        super(message);
    }

    public EnvironmentConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvironmentConfigException(Throwable cause) {
        super(cause);
    }

    public EnvironmentConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EnvironmentConfigException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public EnvironmentConfigException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
