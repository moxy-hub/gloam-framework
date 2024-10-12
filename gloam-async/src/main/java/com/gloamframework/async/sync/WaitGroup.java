package com.gloamframework.async.sync;


import com.gloamframework.async.sync.exception.WaitAddCalledException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阑珊实现
 */
public class WaitGroup implements Wait {

    private CountDownLatch cdl;

    private final AtomicInteger wait;

    private final AtomicInteger done;

    public WaitGroup() {
        wait = new AtomicInteger(0);
        done = new AtomicInteger(0);
    }

    public WaitGroup(int count) {
        this();
        this.add(count);
    }

    /**
     * 增加等待信号量,如果已经处于等待获得发送过信号量,则会抛出WaitAddCalledException异常
     * 该方法的作用是为了方便的使用lamda表达式
     */
    public void add(int delta) {
        synchronized (this) {
            if (cdl != null) {
                throw new WaitAddCalledException();
            }
            wait.addAndGet(delta);
        }
    }

    /**
     * 发出完成信号量
     */
    public void done() {
        synchronized (this) {
            if (cdl == null) {
                done.addAndGet(1);
            } else {
                this.cdl.countDown();
            }
        }

    }

    @Override
    public void await() throws InterruptedException {
        initCDL();
        this.cdl.await();
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        initCDL();
        return this.cdl.await(timeout, unit);
    }

    public long getWait() {
        synchronized (this) {
            if (this.cdl == null) {
                return 0;
            }
        }
        return this.cdl.getCount();
    }

    private void initCDL() {
        synchronized (this) {
            if (this.cdl == null) {
                this.cdl = new CountDownLatch(wait.get());
            }
            for (int i = 0; i < done.get(); i++) {
                this.cdl.countDown();
            }
        }
    }
}