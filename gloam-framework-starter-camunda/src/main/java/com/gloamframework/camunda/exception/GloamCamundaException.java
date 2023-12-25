package com.gloamframework.camunda.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * @author 晓龙
 */
public class GloamCamundaException extends GloamRuntimeException {

    public GloamCamundaException() {
    }

    public GloamCamundaException(String message) {
        super(message);
    }

    public GloamCamundaException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamCamundaException(Throwable cause) {
        super(cause);
    }

    public GloamCamundaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamCamundaException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamCamundaException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
