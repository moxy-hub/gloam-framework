package com.gloamframework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.concurrent.Callable;

/**
 * gloam缓存，对原本的spring cache 进行增加，主要增加过期处理，仅在使用redis的情况下生效过期时间
 *
 * @author 晓龙
 */
@Slf4j
public class GloamCache implements Cache {

    private final Cache cache;

    public GloamCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return cache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return cache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return cache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.evict(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    /**
     * 支持缓存过期的写入
     *
     * @param key    缓存key
     * @param value  缓存值
     * @param expire 过期时间
     */
    public void put(Object key, Object value, long expire) {
        if (!RedisCacheWriter.class.isAssignableFrom(cache.getNativeCache().getClass())) {
            log.warn("current cache type not support value expire,skip it,if you want your value expire,please use redis cache");
            this.put(key, value);
            return;
        }
        this.put(key, new ExpireValue(value, expire));
    }
}
