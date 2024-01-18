package com.gloamframework.async.thread.channel.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

public class ChannelWriteWhenClosedException extends GloamRuntimeException {

    private static final long serialVersionUID = 1L;

    public ChannelWriteWhenClosedException() {
        super("channel write then closed");
    }
}
