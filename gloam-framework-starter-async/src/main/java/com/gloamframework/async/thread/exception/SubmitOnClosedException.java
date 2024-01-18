package com.gloamframework.async.thread.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

public class SubmitOnClosedException extends GloamRuntimeException {

    private static final long serialVersionUID = 1L;

    public SubmitOnClosedException() {
        super("scheduler submit proc on closed ");
    }

}
