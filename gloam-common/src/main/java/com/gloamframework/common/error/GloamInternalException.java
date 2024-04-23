package com.gloamframework.common.error;

import com.gloamframework.common.lang.StringUtil;

/**
 * gloam内部异常，该异常为RuntimeException的扩展
 *
 * @author 晓龙
 */
@SuppressWarnings("unused")
public class GloamInternalException extends RuntimeException {

    public GloamInternalException() {
    }

    public GloamInternalException(String message) {
        super(message);
    }

    public GloamInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GloamInternalException(Throwable cause) {
        super(cause);
    }

    public GloamInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GloamInternalException(String messagePattern, Throwable cause, Object... params) {
        this(StringUtil.format(messagePattern, params), cause);
    }

    public GloamInternalException(String messagePattern, Object... params) {
        this(messagePattern, null, params);
    }
}
