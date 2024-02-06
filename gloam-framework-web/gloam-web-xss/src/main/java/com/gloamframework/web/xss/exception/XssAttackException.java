package com.gloamframework.web.xss.exception;

import com.gloamframework.common.error.GloamInternalException;

public class XssAttackException extends GloamInternalException {

    public XssAttackException(String message, Object... params) {
        super(message, params);
    }

    public XssAttackException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
