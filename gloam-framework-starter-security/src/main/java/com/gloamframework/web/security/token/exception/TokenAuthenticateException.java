package com.gloamframework.web.security.token.exception;

/**
 * token 认证异常
 * @author 晓龙
 */
public class TokenAuthenticateException extends TokenException{
    public TokenAuthenticateException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TokenAuthenticateException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
