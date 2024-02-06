package com.gloamframework.web.security.exception;

import com.gloamframework.core.boot.diagnostics.GloamStartException;

/**
 * web security 适配器配置异常
 *
 * @author 晓龙
 */
public class GloamWebSecurityAdapterApplyException extends GloamStartException {

    public GloamWebSecurityAdapterApplyException(String description, String action, Throwable cause) {
        super(description, action, cause);
    }

}
