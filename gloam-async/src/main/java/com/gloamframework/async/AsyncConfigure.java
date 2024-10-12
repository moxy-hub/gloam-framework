package com.gloamframework.async;

import com.gloamframework.async.thread.GloamThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步支持
 *
 * @author 晓龙
 */
@Configurable
@EnableAsync
@Slf4j
public class AsyncConfigure implements AsyncConfigurer {

    /**
     * 创建全局的线程池，在外部没有指定的情况下，默认使用的实现
     */
    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new GloamThreadPoolTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("[gloam异步线程]:执行失败，目标方法:{},目标参数:{},异常:", method, params, ex);
        };
    }

}
