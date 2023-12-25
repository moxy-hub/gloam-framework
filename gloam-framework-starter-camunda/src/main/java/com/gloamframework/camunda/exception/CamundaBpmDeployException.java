package com.gloamframework.camunda.exception;

/**
 * @author 晓龙
 */
public class CamundaBpmDeployException extends GloamCamundaException {

    public CamundaBpmDeployException() {
    }

    public CamundaBpmDeployException(String message) {
        super(message);
    }

    public CamundaBpmDeployException(String message, Throwable cause) {
        super(message, cause);
    }

    public CamundaBpmDeployException(Throwable cause) {
        super(cause);
    }

    public CamundaBpmDeployException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CamundaBpmDeployException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public CamundaBpmDeployException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
