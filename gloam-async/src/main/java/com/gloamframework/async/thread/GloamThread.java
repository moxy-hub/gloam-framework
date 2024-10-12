package com.gloamframework.async.thread;

import cn.hutool.extra.spring.SpringUtil;
import com.gloamframework.common.error.GloamInternalException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 对于线程池的静态调用
 *
 * @author 晓龙
 */
public class GloamThread {

    private static volatile ThreadPoolTaskExecutor taskExecutor;


    /**
     * runnable方式提交
     */
    public static void go(Runnable run) {
        getExecutor().submit(run);
    }

    /**
     * callable方式提交
     */
    public static <T> Future<T> go(Callable<T> call) {
        return getExecutor().submit(call);
    }

    private static ThreadPoolTaskExecutor getExecutor() {
        if (taskExecutor != null) {
            return taskExecutor;
        }
        ThreadPoolTaskExecutor poolTaskExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);
        if (Objects.isNull(poolTaskExecutor)) {
            throw new GloamInternalException("使用多线程执行时，线程池未配置");
        }
        taskExecutor = poolTaskExecutor;
        return taskExecutor;
    }

}
