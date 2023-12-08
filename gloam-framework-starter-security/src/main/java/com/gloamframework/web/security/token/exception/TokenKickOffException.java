package com.gloamframework.web.security.token.exception;

/**
 * @author 晓龙
 */
public class TokenKickOffException extends TokenException {
    public TokenKickOffException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TokenKickOffException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
