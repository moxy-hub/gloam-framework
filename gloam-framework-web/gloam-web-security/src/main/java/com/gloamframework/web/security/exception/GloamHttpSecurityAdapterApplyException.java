package com.gloamframework.web.security.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * http security 适配器配置异常
 *
 * @author 晓龙
 */
public class GloamHttpSecurityAdapterApplyException extends GloamStartException {

    public GloamHttpSecurityAdapterApplyException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

}
