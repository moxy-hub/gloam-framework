package com.gloamframework.async.thread.channel.exception;


import com.gloamframework.common.error.GloamInternalException;

public class ChannelWaitInterruptedException extends GloamInternalException {

    private static final long serialVersionUID = 1L;

    public ChannelWaitInterruptedException(Throwable t) {
        super(t);
    }

}
