package com.gloamframework.async.sync.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

public class WaitAddCalledException extends GloamRuntimeException {

    private static final long serialVersionUID = 1L;

    public WaitAddCalledException() {
        super("sync: WaitGroup misuse: Add called concurrently with Wait");
    }

}
