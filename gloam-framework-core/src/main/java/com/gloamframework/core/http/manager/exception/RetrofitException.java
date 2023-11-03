package com.gloamframework.core.http.manager.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * @author 晓龙
 */
public class RetrofitException extends GloamStartException {
    public RetrofitException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

    public RetrofitException(String description, String action) {
        super(description, action);
    }

    public RetrofitException(String description) {
        super(description);
    }

    public RetrofitException(String description, String action, Object... params) {
        super(description, action, params);
    }
}
