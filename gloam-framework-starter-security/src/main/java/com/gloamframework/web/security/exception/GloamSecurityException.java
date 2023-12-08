package com.gloamframework.web.security.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常
 *
 * @author 晓龙
 */
@Setter
@Getter
@Accessors(chain = true)
public abstract class GloamSecurityException extends AuthenticationException {

    private int responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public GloamSecurityException(String message, Object... params) {
        super(MessageFormatter.arrayFormat(message, params).getMessage());
    }

    public GloamSecurityException(String message, Throwable cause, Object... params) {
        super(MessageFormatter.arrayFormat(message, params).getMessage(), cause);
    }

}
