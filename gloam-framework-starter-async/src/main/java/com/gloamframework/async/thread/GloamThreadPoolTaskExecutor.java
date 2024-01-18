package com.gloamframework.async.thread;

import com.gloamframework.async.sync.Once;
import com.gloamframework.async.thread.channel.Channel;
import com.gloamframework.async.thread.exception.SubmitOnClosedException;
import com.gloamframework.core.exception.GloamRuntimeException;
import com.gloamframework.core.lang.function.Process;
import com.gloamframework.core.lang.function.ProcessWithRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author 晓龙
 */
@Slf4j
@SuppressWarnings("all")
public class GloamThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {


    private final int maxConcurrent;

    private final int warnThreadNum;

    private final Once closeOnce;

    private final Channel<Boolean> limiter;

    public GloamThreadPoolTaskExecutor() {
        this(
                /*
                 * 最大执行为当前机器的核心数
                 */
                Runtime.getRuntime().availableProcessors(),

                /*
                 * 最大容量为核心数的10
                 */
                Runtime.getRuntime().availableProcessors() * 10,

                /*
                 * 线程的等待最长的时间为150秒
                 */
                150,

                /*
                 * 队列为0时使用的为 SynchronousQueue
                 * 如果大于0，则使用 LinkedBlockingQueue<>(queueCapacity)
                 */
                0,

                /*
                 * AbortPolicy:该策略是线程池的默认策略。使用该策略时，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
                 * DiscardPolicy:这个策略和AbortPolicy的slient版本，如果线程池队列满了，会直接丢掉这个任务并且不会有任何异常。
                 * DiscardOldestPolicy:这个策略从字面上也很好理解，丢弃最老的。也就是说如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列。
                 *                     因为队列是队尾进，队头出，所以队头元素是最老的，因此每次都是移除对头元素后再尝试入队。
                 * CallerRunsPolicy:使用此策略，如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行。就像是个急脾气的人，我等不到别人来做这件事就干脆自己干。
                 */
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public GloamThreadPoolTaskExecutor(int maxProcessors, int maxConcurrent, int idleSecondTime, int queueCapacity, RejectedExecutionHandler rejectedExecutionHandler) {
        maxProcessors = maxProcessors <= 0 ? 1 : maxProcessors;
        this.maxConcurrent = maxConcurrent <= 0 ? 10000 : maxConcurrent;
        this.warnThreadNum = (int) ((double) this.maxConcurrent * 0.8);
        closeOnce = new Once();
        limiter = new Channel<>(this.maxConcurrent);
        // 配置核心线程数
        super.setCorePoolSize(maxProcessors);
        // 配置最大线程数
        super.setMaxPoolSize(maxConcurrent);
        // 线程活跃时间（秒）
        super.setKeepAliveSeconds(idleSecondTime);
        // 配置队列大小
        super.setQueueCapacity(queueCapacity);
        // 线程的名称前缀
        super.setThreadNamePrefix("Gloam-Task-");
        // 等待所有任务结束后再关闭线程池
        super.setWaitForTasksToCompleteOnShutdown(true);
        // 设置拒绝策略
        super.setRejectedExecutionHandler(rejectedExecutionHandler);
        // 执行初始化
        super.initialize();
    }

    @Override
    public void execute(Runnable task) {
        log.debug("[gloam sync]:执行任务:{}", task);
        this.go(() -> super.execute(readRunnableTask(task)));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        log.debug("[gloam sync]:执行任务:{}", task);
        this.go(() -> super.execute(readRunnableTask(task), startTimeout));
    }

    @Override
    public Future<?> submit(Runnable task) {
        log.debug("[gloam sync]:执行任务:{}", task);
        return this.go(() -> super.submit(readRunnableTask(task)));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        log.debug("[gloam sync]:执行任务:{}", task);
        return this.go(() -> super.submit(readCallTask(task)));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        log.debug("[gloam sync]:执行任务:{}", task);
        return this.go(() -> super.submitListenable(readRunnableTask(task)));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return this.go(() -> super.submitListenable(readCallTask(task)));
    }

    private <T> Callable<T> readCallTask(Callable<T> task) {
        return () -> {
            try {
                if (Thread.interrupted()) {
                    return null;
                }
                log.debug("[gloam sync]:执行任务:{}", task);
                return task.call();
            } catch (Exception e) {
                log.error("Goroutine Process Error", e);
                return null;
            } finally {
                limiter.read();
            }
        };
    }

    private Runnable readRunnableTask(Runnable task) {
        return () -> {
            try {
                if (Thread.interrupted()) {
                    return;
                }
                task.run();
            } catch (Exception e) {
                log.error("Goroutine Process Error", e);
            } finally {
                limiter.read();
            }
        };
    }

    /**
     * 执行线程任务
     *
     * @param process 任务
     */
    private <T> T go(ProcessWithRes<T> process) {
        try {
            if (closeOnce.isDone()) {
                throw new SubmitOnClosedException();
            }
            if (limiter.size() >= this.warnThreadNum) {
                log.debug("running out of thread resources,left {},total {},thread alive {}",
                        this.maxConcurrent - limiter.size(), this.maxConcurrent, super.getActiveCount());
            }
            limiter.write(true);
            // 把请求异步保存
            RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
            return process.doProcess();
        } catch (Throwable e) {
            throw new GloamRuntimeException("线程任务执行失败", e);
        }
    }

    private void go(Process process) {
        // 执行不没有返回值
        this.go((ProcessWithRes<Void>) () -> {
            process.doProcess();
            return null;
        });
    }

    @Override
    public void shutdown() {
        closeOnce.process(() -> {
            super.shutdown();
            limiter.close();
        });
    }

}
