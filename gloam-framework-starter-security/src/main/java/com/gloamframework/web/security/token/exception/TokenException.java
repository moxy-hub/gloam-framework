package com.gloamframework.web.security.token.exception;

import com.gloamframework.web.security.exception.GloamSecurityException;
import org.slf4j.helpers.MessageFormatter;

/**
 * token模块父级异常
 *
 * @author 晓龙
 */
public abstract class TokenException extends GloamSecurityException {

    public TokenException(String messagePattern, Throwable t, Object... params) {
        super(MessageFormatter.arrayFormat(messagePattern, params).getMessage(), t);
    }

    public TokenException(String messagePattern, Object... params) {
        super(MessageFormatter.arrayFormat(messagePattern, params).getMessage());
    }
}
