package com.gloamframework.file.exception;

/**
 * @author 晓龙
 */
public class FileUploadException extends FileException {

    public FileUploadException() {
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(Throwable cause) {
        super(cause);
    }

    public FileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileUploadException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FileUploadException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
