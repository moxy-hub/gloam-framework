package com.gloamframework.file.exception;

/**
 * @author 晓龙
 */
public class FileDownloadException extends FileException {

    public FileDownloadException() {
    }

    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadException(Throwable cause) {
        super(cause);
    }

    public FileDownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileDownloadException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public FileDownloadException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
