package com.gloamframework.async.sync;


import com.gloamframework.core.lang.function.Process;
import com.gloamframework.core.lang.function.ProcessWithRes;

import java.util.concurrent.TimeUnit;

public interface Lock {

    /**
     * 试图马上进行锁,如果有其他线程持有锁,则立即返回失败,否则立刻持有锁
     */
    boolean lockNow();

    /**
     * 在预期的时间内获得锁,返回时候申请锁成功,该方法可能出现中断异常
     */
    boolean lock(long requireTimeout, TimeUnit unit) throws InterruptedException;

    /**
     * 申请锁,如果一致没有获得锁机会则一致阻塞,该方法可能出现中断异常
     */
    void lock() throws InterruptedException;

    /**
     * 自动释放锁
     */
    default Throwable lockThenAutoUnlock(Process p) {
        try {
            this.lock();
            if (p != null) {
                p.doProcess();
            }
            return null;
        } catch (Exception e) {
            return e;
        } finally {
            this.unlock();
        }
    }

    default <T> T lockThenAutoUnlock(ProcessWithRes<T> p) {
        try {
            this.lock();
            if (p != null) {
                return p.doProcess();
            }
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            this.unlock();
        }
    }

    default Throwable lockThenAutoUnlock(long requireTimeout, TimeUnit unit, Process p) throws InterruptedException {
        if (!this.lock(requireTimeout, unit)) {
            return null;
        }
        try {
            if (p != null) {
                p.doProcess();
            }
            return null;
        } catch (Exception e) {
            return e;
        } finally {
            this.unlock();
        }
    }

    default <T> T lockThenAutoUnlock(long requireTimeout, TimeUnit unit, ProcessWithRes<T> p) throws InterruptedException {
        if (!this.lock(requireTimeout, unit)) {
            return null;
        }
        try {
            if (p != null) {
                return p.doProcess();
            }
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            this.unlock();
        }
    }

    /**
     * 释放锁,返回是否释放成功,如果当前线程不持有锁,而调用该方法,将会抛出异常
     */
    void unlock();

}
