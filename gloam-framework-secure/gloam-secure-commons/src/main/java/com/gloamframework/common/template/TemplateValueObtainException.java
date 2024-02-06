package com.gloamframework.common.template;

/**
 * @author 晓龙
 */
public class TemplateValueObtainException extends RuntimeException {

    public TemplateValueObtainException() {
    }

    public TemplateValueObtainException(String message) {
        super(message);
    }

    public TemplateValueObtainException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateValueObtainException(Throwable cause) {
        super(cause);
    }

    public TemplateValueObtainException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
