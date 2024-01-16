package com.gloamframework.file.exception;

/**
 * @author 晓龙
 */
public class FileParamException extends FileException {
    public FileParamException() {
    }

    public FileParamException(String message) {
        super(message);
    }

    public FileParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileParamException(Throwable cause) {
        super(cause);
    }

    public FileParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileParamException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FileParamException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
