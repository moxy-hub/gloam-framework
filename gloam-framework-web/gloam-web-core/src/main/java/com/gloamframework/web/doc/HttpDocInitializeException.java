package com.gloamframework.web.doc;

import com.gloamframework.core.boot.diagnostics.GloamStartException;


public class HttpDocInitializeException extends GloamStartException {
    public HttpDocInitializeException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

    public HttpDocInitializeException(String description, String action) {
        super(description, action);
    }

    public HttpDocInitializeException(String description) {
        super(description);
    }

    public HttpDocInitializeException(String description, String action, Object... params) {
        super(description, action, params);
    }
}
