package com.gloamframework.async.thread.exception;

import com.gloamframework.common.error.GloamInternalException;

public class SubmitOnClosedException extends GloamInternalException {

    private static final long serialVersionUID = 1L;

    public SubmitOnClosedException() {
        super("scheduler submit proc on closed ");
    }

}
