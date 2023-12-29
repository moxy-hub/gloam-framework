package com.gloamframework.file.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

/**
 * @author 晓龙
 */
public abstract class FileException extends GloamRuntimeException {

    public FileException() {
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FileException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
