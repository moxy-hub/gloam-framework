package com.gloamframework.web.security.token.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * token 认证异常
 *
 * @author 晓龙
 */
public class TokenAuthenticateException extends TokenException {
    public TokenAuthenticateException(String messagePattern, Throwable t, Object... params) {
        super(messagePattern, t, params);
        super.setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public TokenAuthenticateException(String messagePattern, Object... params) {
        super(messagePattern, params);
        super.setResponseStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
