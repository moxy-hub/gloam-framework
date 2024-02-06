package com.gloamframework.data.tenant.exception;

import com.gloamframework.web.security.exception.GloamSecurityException;

/**
 * @author 晓龙
 */
public class TenantException extends GloamSecurityException {
    public TenantException(String message, Object... params) {
        super(message, params);
    }

    public TenantException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
