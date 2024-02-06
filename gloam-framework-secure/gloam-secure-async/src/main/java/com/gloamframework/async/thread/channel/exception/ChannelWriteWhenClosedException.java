package com.gloamframework.async.thread.channel.exception;

import com.gloamframework.common.error.GloamInternalException;

public class ChannelWriteWhenClosedException extends GloamInternalException {

    private static final long serialVersionUID = 1L;

    public ChannelWriteWhenClosedException() {
        super("channel write then closed");
    }
}
