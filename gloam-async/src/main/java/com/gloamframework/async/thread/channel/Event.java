package com.gloamframework.async.thread.channel;

@FunctionalInterface
interface Event {
    enum Type {
        Read, Write, Closed
    }

    void onChannelEvent(Type type);
}
