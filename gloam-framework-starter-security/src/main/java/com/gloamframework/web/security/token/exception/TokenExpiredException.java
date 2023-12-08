package com.gloamframework.web.security.token.exception;

/**
 * @author 晓龙
 */
public class TokenExpiredException extends TokenException {
    public TokenExpiredException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TokenExpiredException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
