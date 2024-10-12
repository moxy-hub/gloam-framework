package com.gloamframework.async.sync;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 进程内只进行一次的操作
 */
public class Once {

    private final AtomicBoolean isDone = new AtomicBoolean(false);

    public void process(OnceProcess op) {
        if (op == null) {
            return;
        }
        if (!isDone.getAndSet(true)) {
            op.process();
        }
    }

    public boolean isDone() {
        return this.isDone.get();
    }

    @FunctionalInterface
    public interface OnceProcess {
        void process();
    }
}
