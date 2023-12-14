package com.gloamframework.web.security.envelope.exception;

import com.gloamframework.web.security.exception.GloamSecurityException;

/**
 * 信封分析错误
 * @author 晓龙
 */
public class EnvelopeAnalysisException extends GloamSecurityException {

    public EnvelopeAnalysisException(String message, Object... params) {
        super(message, params);
    }

    public EnvelopeAnalysisException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
