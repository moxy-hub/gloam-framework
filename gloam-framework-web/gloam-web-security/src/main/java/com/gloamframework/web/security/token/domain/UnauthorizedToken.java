package com.gloamframework.web.security.token.domain;

/**
 * 未授权的token
 *
 * @author 晓龙
 */
public class UnauthorizedToken extends Token {
    public UnauthorizedToken() {
        super("", "", null);
    }
}
