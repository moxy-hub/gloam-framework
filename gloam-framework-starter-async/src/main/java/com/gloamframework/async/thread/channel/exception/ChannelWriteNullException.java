package com.gloamframework.async.thread.channel.exception;

import com.gloamframework.core.exception.GloamRuntimeException;

public class ChannelWriteNullException extends GloamRuntimeException {

    private static final long serialVersionUID = 1L;

    public ChannelWriteNullException() {
        super("channel write null object");
    }
}
