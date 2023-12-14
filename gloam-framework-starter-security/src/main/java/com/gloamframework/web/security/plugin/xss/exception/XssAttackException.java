package com.gloamframework.web.security.plugin.xss.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

public class XssAttackException extends GloamRuntimeException {

    public XssAttackException(String message, Object... params) {
        super(message, params);
    }

    public XssAttackException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
