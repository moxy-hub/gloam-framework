package com.gloamframework.web.security.chain.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * 链初始化异常
 *
 * @author 晓龙
 */
public class SecurityChainInitException extends GloamStartException {
    public SecurityChainInitException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

    public SecurityChainInitException(String description, String action) {
        super(description, action);
    }

    public SecurityChainInitException(String description) {
        super(description);
    }

    public SecurityChainInitException(String description, String action, Object... params) {
        super(description, action, params);
    }
}
