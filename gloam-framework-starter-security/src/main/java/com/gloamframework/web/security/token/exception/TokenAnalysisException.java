package com.gloamframework.web.security.token.exception;

/**
 * token解析异常
 *
 * @author 晓龙
 */
public class TokenAnalysisException extends TokenException {
    public TokenAnalysisException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
    }

    public TokenAnalysisException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
