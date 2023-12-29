package com.gloamframework.file.exception;

/**
 * @author 晓龙
 */
public class FileDeleteException extends FileException {
    public FileDeleteException() {
    }

    public FileDeleteException(String message) {
        super(message);
    }

    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteException(Throwable cause) {
        super(cause);
    }

    public FileDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileDeleteException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FileDeleteException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
