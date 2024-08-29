package com.gloamframework.data.redis.distributed.aspect;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 分布式锁key
     */
    @AliasFor("value")
    String lockKey() default "";

    @AliasFor("lockKey")
    String value() default "";

    /**
     * 过期时间
     */
    int timeout() default 30;

    /**
     * 过期时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}
