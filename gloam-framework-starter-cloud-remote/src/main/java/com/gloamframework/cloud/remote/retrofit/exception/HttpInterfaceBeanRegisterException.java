package com.gloamframework.cloud.remote.retrofit.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * @author 晓龙
 */
public class HttpInterfaceBeanRegisterException extends GloamStartException {

    public HttpInterfaceBeanRegisterException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

    public HttpInterfaceBeanRegisterException(String description, String action) {
        super(description, action);
    }

    public HttpInterfaceBeanRegisterException(String description) {
        super(description);
    }

    public HttpInterfaceBeanRegisterException(String description, String action, Object... params) {
        super(description, action, params);
    }
}
