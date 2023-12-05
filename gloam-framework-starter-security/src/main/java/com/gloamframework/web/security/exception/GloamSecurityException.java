package com.gloamframework.web.security.exception;

import com.gloamframework.core.exception.GloamRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常
 *
 * @author 晓龙
 */
@Setter
@Getter
@Accessors(chain = true)
public abstract class GloamSecurityException extends GloamRuntimeException {

    private int responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public GloamSecurityException() {
    }

    public GloamSecurityException(String message) {
        super(message);
    }

    public GloamSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamSecurityException(Throwable cause) {
        super(cause);
    }

    public GloamSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamSecurityException(String messagePattern, Throwable cause, Object... params) {
        super(messagePattern, cause, params);
    }

    public GloamSecurityException(String messagePattern, Object... params) {
        super(messagePattern, params);
    }
}
