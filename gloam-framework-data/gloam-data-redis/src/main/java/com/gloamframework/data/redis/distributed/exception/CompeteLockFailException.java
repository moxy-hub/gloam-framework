package com.gloamframework.data.redis.distributed.exception;

import com.gloamframework.common.error.GloamInternalException;

public class CompeteLockFailException extends GloamInternalException {

    public CompeteLockFailException() {
    }

    public CompeteLockFailException(String message) {
        super(message);
    }

    public CompeteLockFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompeteLockFailException(Throwable cause) {
        super(cause);
    }

    public CompeteLockFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CompeteLockFailException(String message, String... params) {
        super(message, params);
    }
}
