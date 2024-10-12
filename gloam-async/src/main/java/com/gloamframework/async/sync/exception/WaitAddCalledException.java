package com.gloamframework.async.sync.exception;

import com.gloamframework.common.error.GloamInternalException;

public class WaitAddCalledException extends GloamInternalException {

    private static final long serialVersionUID = 1L;

    public WaitAddCalledException() {
        super("sync: WaitGroup misuse: Add called concurrently with Wait");
    }

}
