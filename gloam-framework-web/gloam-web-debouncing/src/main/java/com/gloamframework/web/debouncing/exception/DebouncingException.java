package com.gloamframework.web.debouncing.exception;

import com.gloamframework.web.security.exception.GloamSecurityException;

/**
 * 防抖异常
 *
 * @author 晓龙
 */
public class DebouncingException extends GloamSecurityException {

    public DebouncingException(String message, Object... params) {
        super(message, params);
    }

    public DebouncingException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
