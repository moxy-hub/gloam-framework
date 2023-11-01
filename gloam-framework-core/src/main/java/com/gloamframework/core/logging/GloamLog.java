package com.gloamframework.core.logging;

import org.apache.commons.logging.Log;
import org.springframework.boot.logging.DeferredLog;

/**
 * 日志服务，提供唯一带有存储的日志
 * 用于日志系统还没有初始化  使用DeferredLog来记录  并在环境准备好进行回放
 *
 * @author 晓龙
 */
public class GloamLog {

    /**
     * 具有回放能力的log
     */
    private static final DeferredLog log = new DeferredLog();

    /**
     * 获取日志
     */
    public static Log getLogger() {
        return log;
    }

    /**
     * 回放
     *
     * @param destination 目标类
     */
    public static void replayTo(Class<?> destination) {
        log.replayTo(destination);
    }
}
