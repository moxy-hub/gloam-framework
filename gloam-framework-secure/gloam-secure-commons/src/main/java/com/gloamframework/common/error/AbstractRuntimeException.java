package com.gloamframework.common.error;

import java.util.Objects;

/**
 * 异常基类
 *
 * @author 晓龙
 */
public abstract class AbstractRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public AbstractRuntimeException() {
        this.errorCode = filterErrorCode(null);
    }

    public AbstractRuntimeException(Throwable cause) {
        super(cause);
        this.errorCode = filterErrorCode(null);
    }

    public AbstractRuntimeException(ErrorCode errorCode) {
        this.errorCode = filterErrorCode(errorCode);
    }

    public AbstractRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(filterErrorCode(errorCode).getMessage(), cause);
        this.errorCode = filterErrorCode(errorCode);
    }

    public AbstractRuntimeException(ErrorCode errorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(filterErrorCode(errorCode).getMessage(), cause, enableSuppression, writableStackTrace);
        this.errorCode = filterErrorCode(errorCode);
    }

    private static ErrorCode filterErrorCode(ErrorCode errorCode) {
        if (Objects.isNull(errorCode)) {
            errorCode = ErrorCode.DEFAULT_ERROR_CODE;
        }
        return errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }
}
