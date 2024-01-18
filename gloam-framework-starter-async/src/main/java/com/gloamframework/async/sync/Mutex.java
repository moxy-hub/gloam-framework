package com.gloamframework.async.sync;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 互斥锁
 */
public class Mutex extends AbstractLock {

    public Mutex() {
        super(new ReentrantLock(false));
    }

}
