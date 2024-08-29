package com.gloamframework.data.redis.distributed;

import cn.hutool.core.util.IdUtil;
import com.gloamframework.core.lang.function.Process;
import com.gloamframework.core.lang.function.ProcessWithRes;
import com.gloamframework.data.redis.RedisUtil;
import com.gloamframework.data.redis.distributed.exception.CompeteLockFailException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 */
@Slf4j
public class RedisDistributedLock {

    private static final int TTL = 30;

    public static <T> T process(String lockKey, ProcessWithRes<T> process) throws Throwable {
        return process(lockKey, TTL, TimeUnit.SECONDS, process);
    }

    /**
     * redis分布式锁
     *
     * @param lockKey 锁key
     * @param process 程序执行
     */
    public static <T> T process(String lockKey, int ttl, TimeUnit timeUnit, ProcessWithRes<T> process) throws Throwable {
        String operatorClient = IdUtil.randomUUID();
        try {
            boolean lock = RedisUtil.LockOps.getLock(lockKey, operatorClient, ttl, timeUnit, true);
            // ========= setnx 竞争锁失败 ==========
            if (!lock) {
                throw new CompeteLockFailException("redis获取锁失败,key:" + lockKey);
            }
            // ========== setnx 竞争锁成功 =======
            log.info("[Redis Lock]:获取到分布式锁:{}", lockKey);
            // 执行业务代码
            return process.doProcess();
        } finally {
            // 防止异常导致锁无法释放！！！需要释放这个lockKey
            // 如果lockKey对应的value是本次请求设置的value，才允许释放锁！
            // 防止其他进程释放掉当前的锁
            RedisUtil.LockOps.releaseLock(lockKey, operatorClient);
        }
    }

    public static void process(String lockKey, Process process) throws Throwable {
        process(lockKey, TTL, TimeUnit.SECONDS, process);
    }

    public static void process(String lockKey, int ttl, TimeUnit timeUnit, Process process) throws Throwable {
        process(lockKey, ttl, timeUnit, () -> {
            process.doProcess();
            return null;
        });
    }


}
