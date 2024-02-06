package com.gloamframework.async.sync;

import java.util.concurrent.TimeUnit;

abstract class AbstractLock implements Lock {

    private final java.util.concurrent.locks.Lock realLock;

    public AbstractLock(java.util.concurrent.locks.Lock realLock) {
        this.realLock = realLock;
    }

    @Override
    public boolean lockNow() {
        return realLock.tryLock();
    }

    @Override
    public boolean lock(long requireTimeout, TimeUnit unit) throws InterruptedException {
        if (requireTimeout <= 0) {
            realLock.lockInterruptibly();
            return true;
        }
        return realLock.tryLock(requireTimeout, unit);
    }

    @Override
    public void lock() throws InterruptedException {
        realLock.lockInterruptibly();
    }

    @Override
    public void unlock() {
        realLock.unlock();
    }

}
