package com.gloamframework.async.thread.channel;


import com.gloamframework.async.sync.CondLock;
import com.gloamframework.async.sync.Mutex;
import com.gloamframework.async.thread.channel.exception.ChannelWaitInterruptedException;
import com.gloamframework.async.thread.channel.exception.ChannelWriteNullException;
import com.gloamframework.async.thread.channel.exception.ChannelWriteWhenClosedException;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unchecked")
public final class Channel<T> implements Closeable {

    static Object nullMsg = new Object();

    private final LinkedBlockingQueue<Object> queue;

    private CondLock condLock;

    private final AtomicBoolean closed;

    private final AtomicLong readCount;

    private final AtomicLong writeCount;

    private final Set<Event> events;

    private final Mutex lock;

    public Channel() {
        this(0);
    }

    public Channel(int capacity) {
        if (capacity == 0) {
            capacity = 1;
            condLock = new CondLock();
        }
        queue = new LinkedBlockingQueue<Object>(capacity);
        closed = new AtomicBoolean(false);
        readCount = new AtomicLong(0L);
        writeCount = new AtomicLong(0L);
        events = new HashSet<>();
        lock = new Mutex();
    }

    public static <R> Channel<R> New() {
        return new Channel<>(0);
    }

    public static <R> Channel<R> New(int capacity) {
        return new Channel<>(capacity);
    }

    public T read() {
        if (isClosed() && this.queue.isEmpty()) {
            return null;
        }
        try {
            readCount.incrementAndGet();
            Object res = queue.take();
            fireEvents(Event.Type.Read);
            if (condLock != null && queue.isEmpty()) {
                condLock.signal();
            }
            return res == nullMsg ? null : (T) res;
        } catch (InterruptedException e) {
            throw new ChannelWaitInterruptedException(e);
        } finally {
            readCount.decrementAndGet();
        }
    }

    public T tryRead() {
        if (isClosed()) {
            return null;
        }
        T res = (T) queue.poll();
        if (res != null) {
            fireEvents(Event.Type.Read);
            if (condLock != null && queue.isEmpty()) {
                condLock.signal();
            }
        }
        return res;
    }

    public void write(T t) {
        this.write(t, true);
    }

    public boolean write(T t, boolean throwable) {
        try {
            if (t == null) {
                throw new ChannelWriteNullException();
            }
            if (isClosed()) {
                if (throwable) {
                    throw new ChannelWriteWhenClosedException();
                }
                return false;
            }
            try {
                lock.lock();
                writeCount.incrementAndGet();
                queue.put(t);
                fireEvents(Event.Type.Write);
                if (condLock != null && !queue.isEmpty()) {
                    condLock.await();
                }
                return true;
            } catch (InterruptedException e) {
                throw new ChannelWaitInterruptedException(e);
            }
        } finally {
            writeCount.decrementAndGet();
            lock.unlock();
        }
    }

    public boolean isClosed() {
        return closed.get();
    }

    public void close() {
        this.close(false);
    }

    public void close(boolean retain) {
        if (closed.compareAndSet(false, true)) {

            if (condLock != null) {
                condLock.broadcast();
            }
            long reads;
            while ((reads = readCount.get()) > 0) {
                try {
                    if (queue.isEmpty()) {
                        for (long i = 0; i < reads; i++) {
                            if (!queue.offer(nullMsg, 10, TimeUnit.MILLISECONDS)) {
                                break;
                            }
                        }
                    } else {
                        Thread.sleep(reads * 5);
                    }
                } catch (InterruptedException e) {
                    // nothing to do
                }
            }
            long writes;
            while ((writes = writeCount.get()) > 0) {
                try {
                    if (!queue.isEmpty()) {
                        for (long i = 0; i < writes; i++) {
                            if (queue.poll(10, TimeUnit.MILLISECONDS) == null) {
                                break;
                            }
                        }
                    } else {
                        Thread.sleep(reads * 5);
                    }
                } catch (InterruptedException e) {
                    // nothing to do
                }
            }
            fireEvents(Event.Type.Closed);
            try {
                lock.lock();
            } catch (Exception ignore) {
            }
            if (!retain) {
                events.clear();
                queue.clear();
            }
            lock.unlock();
        }
    }

    private void fireEvents(Event.Type type) {
        if (isClosed() && events.isEmpty()) {
            return;
        }
        for (Event e : events) {
            e.onChannelEvent(type);
        }
    }

    public int size() {
        return this.queue.size();
    }

    public int capacity() {
        return this.size() + this.remainingCapacity();
    }

    public int remainingCapacity() {
        return this.queue.remainingCapacity();
    }

    /**
     * 返回是否注册成功
     */
    boolean addListener(Event e) {
        if (e == null || isClosed()) {
            return false;
        }
        synchronized (this) {
            this.events.add(e);
        }
        return true;
    }

    void delListener(Event e) {
        if (e == null || isClosed()) {
            return;
        }
        synchronized (this) {
            this.events.remove(e);
        }
    }

}
