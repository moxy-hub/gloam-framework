package com.gloamframework.async.thread.channel.exception;

import com.gloamframework.common.error.GloamInternalException;

public class ChannelWriteNullException extends GloamInternalException {

    private static final long serialVersionUID = 1L;

    public ChannelWriteNullException() {
        super("channel write null object");
    }
}
