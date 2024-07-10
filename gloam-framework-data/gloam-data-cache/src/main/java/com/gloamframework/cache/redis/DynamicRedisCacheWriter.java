package com.gloamframework.cache.redis;

import com.gloamframework.cache.properties.CacheProperties;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * 替换遍历策略KEYS为SCAN
 *
 * @author 晓龙
 */
public class DynamicRedisCacheWriter implements RedisCacheWriter {

    private final RedisCacheWriter defaultWriter;
    private final RedisConnectionFactory connectionFactory;
    private final CacheProperties cacheProperties;

    public DynamicRedisCacheWriter(RedisCacheWriter defaultWriter, RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {
        this.defaultWriter = defaultWriter;
        this.connectionFactory = connectionFactory;
        this.cacheProperties = cacheProperties;
    }

    @Override
    public void put(String name, byte[] key, byte[] value, Duration ttl) {
        defaultWriter.put(name, key, value, ttl);
    }

    @Override
    @Nullable
    public byte[] get(String name, byte[] key) {
        return defaultWriter.get(name, key);
    }

    @Override
    public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
        return defaultWriter.putIfAbsent(name, key, value, ttl);
    }

    @Override
    public void remove(String name, byte[] key) {
        defaultWriter.remove(name, key);
    }

    @Override
    public void clean(String name, byte[] pattern) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");

        try (RedisConnection connection = connectionFactory.getConnection()) {
            Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().count(cacheProperties.getScanCount()).match(new String(pattern)).build());
            Set<byte[]> ks = new HashSet<>();
            while (cursor.hasNext()) {
                ks.add(cursor.next());
            }
            byte[][] keys = ks.toArray(new byte[0][]);
            if (keys.length > 0) {
                connection.del(keys);
            }
        }
    }
}
