package com.gloamframework.async.sync;

import java.util.concurrent.TimeUnit;

public interface Wait {

    /**
     * 等待唤醒,直到出现中断为止
     */
    void await() throws InterruptedException;

    /**
     * 等待唤醒,在规定的时间内没有得到信号通知,则返回false,可能出现中断异常
     */
    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

}
