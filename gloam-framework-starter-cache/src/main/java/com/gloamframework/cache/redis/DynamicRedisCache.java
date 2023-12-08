package com.gloamframework.cache.redis;

import cn.hutool.core.util.RandomUtil;
import com.gloamframework.cache.ExpireValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

class DynamicRedisCache extends RedisCache {

    @FunctionalInterface
    interface ValueAnalysis<T> {
        T analysis(Duration ttl, Object redisValue);
    }

    private static final int HOUR = 60 * 60;
    private static final int DAY = 24;

    protected DynamicRedisCache(RedisCache redisCache) {
        this(redisCache.getName(), redisCache.getNativeCache(), redisCache.getCacheConfiguration());
    }

    protected DynamicRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format("Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.", getName()));
        }
        // 判断传入的对象，是否为支持设置过期时间的对象
        analysisValue(value, ((ttl, redisValue) -> {
            getNativeCache().put(getName(), serializeCacheKey(createCacheKey(key)), serializeCacheValue(redisValue), ttl);
            return true;
        }));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }
        // 判断传入的对象，是否为支持设置过期时间的对象
        return this.analysisValue(value, ((ttl, redisValue) -> {
            byte[] result = getNativeCache().putIfAbsent(getName(), serializeCacheKey(createCacheKey(key)), serializeCacheValue(redisValue), ttl);
            return result == null ? null : new SimpleValueWrapper(this.fromStoreValue(this.deserializeCacheValue(result)));
        }));
    }

    private Duration getDynamicTtl() {
        // 设置动态失效时间
        int randomTTL = RandomUtil.randomInt(HOUR, HOUR * DAY);
        return Duration.ofSeconds(randomTTL);
    }

    private <T> T analysisValue(Object value, ValueAnalysis<T> valueAnalysis) {
        Duration ttl;
        Object redisValue;
        if (ExpireValue.class.isAssignableFrom(value.getClass())) {
            ExpireValue expireValue = (ExpireValue) value;
            ttl = Duration.ofMillis(expireValue.getExpire());
            redisValue = expireValue.getValue();
        } else {
            ttl = this.getDynamicTtl();
            redisValue = value;
        }
        return valueAnalysis.analysis(ttl, redisValue);
    }
}
