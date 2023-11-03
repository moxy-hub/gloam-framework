package com.gloamframework.core.http.manager.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * @author 晓龙
 */
public class OkHttpClientException extends GloamStartException {
    public OkHttpClientException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

    public OkHttpClientException(String description, String action) {
        super(description, action);
    }

    public OkHttpClientException(String description) {
        super(description);
    }

    public OkHttpClientException(String description, String action, Object... params) {
        super(description, action, params);
    }
}
