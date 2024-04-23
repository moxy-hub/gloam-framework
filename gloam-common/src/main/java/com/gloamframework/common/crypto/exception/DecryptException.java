package com.gloamframework.common.crypto.exception;

/**
 * @author 晓龙
 */
public class DecryptException extends Exception {

    public DecryptException() {
    }

    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptException(Throwable cause) {
        super(cause);
    }

    public DecryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
