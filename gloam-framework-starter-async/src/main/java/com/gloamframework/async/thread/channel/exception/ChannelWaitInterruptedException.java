package com.gloamframework.async.thread.channel.exception;


import com.gloamframework.core.exception.GloamRuntimeException;

public class ChannelWaitInterruptedException extends GloamRuntimeException {

    private static final long serialVersionUID = 1L;

    public ChannelWaitInterruptedException(Throwable t) {
        super(t);
    }

}
