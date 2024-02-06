package com.gloamframework.common.error;

/**
 * 业务错误，该异常会被spring mvc处理，返回到前端
 *
 * @author 晓龙
 */
public class GloamBusinessException extends AbstractRuntimeException {

    public GloamBusinessException() {
    }

    public GloamBusinessException(Throwable cause) {
        super(cause);
    }

    public GloamBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GloamBusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public GloamBusinessException(ErrorCode errorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, cause, enableSuppression, writableStackTrace);
    }
}
