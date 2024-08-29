package com.gloamframework.data.redis.distributed.aspect;

import com.gloamframework.data.redis.distributed.RedisDistributedLock;
import com.gloamframework.data.redis.distributed.exception.CompeteLockFailException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class DistributedLockAspect {

    @Around("@annotation(com.gloamframework.data.redis.distributed.aspect.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的分布式注解
        DistributedLock distributedLock = AnnotatedElementUtils.getMergedAnnotation(method, DistributedLock.class);
        if (distributedLock == null) {
            log.error("[Redis Lock]:获取分布式注解失败");
            throw new CompeteLockFailException("[Redis Lock]:获取分布式注解失败");
        }
        String lockKey = distributedLock.lockKey();
        if (StringUtils.isBlank(lockKey)) {
            throw new CompeteLockFailException("[Redis Lock]:获取分布式锁的key失败");
        }
        return RedisDistributedLock.process(lockKey, distributedLock.timeout(), distributedLock.unit(), () -> joinPoint.proceed());
    }

}
