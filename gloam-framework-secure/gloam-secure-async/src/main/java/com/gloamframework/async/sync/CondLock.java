package com.gloamframework.async.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件锁,基于ReentrantLock实现
 */
public class CondLock implements Wait {

    private final ReentrantLock lock;

    private final Condition condition;

    public CondLock() {
        lock = new ReentrantLock(false);
        condition = lock.newCondition();
    }

    /**
     * 唤醒一个等待线程
     */
    public void signal() {
        try {
            lock.lock();
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 唤醒所有等待线程
     */
    public void broadcast() {
        try {
            lock.lock();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void await() throws InterruptedException {
        try {
            lock.lock();
            condition.await();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            long deadline = System.currentTimeMillis() + unit.toMillis(timeout);
            if (!lock.tryLock(timeout, unit)) {
                return false;
            }
            long waitTime = deadline - System.currentTimeMillis();
            if (waitTime > 0) {
                return condition.await(waitTime, unit);
            }
            return true;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
