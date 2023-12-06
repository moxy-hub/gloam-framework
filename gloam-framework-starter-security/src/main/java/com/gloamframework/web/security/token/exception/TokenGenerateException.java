package com.gloamframework.web.security.token.exception;

/**
 * token生成异常
 * @author 晓龙
 */
public class TokenGenerateException extends TokenException{
    public TokenGenerateException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TokenGenerateException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
