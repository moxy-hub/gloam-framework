package com.gloamframework.web.security.feign;

import com.gloamframework.web.security.exception.GloamSecurityException;

/**
 * @author 晓龙
 */
public class FeignFilterException extends GloamSecurityException {
    public FeignFilterException(String message, Object... params) {
        super(message, params);
    }

    public FeignFilterException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
