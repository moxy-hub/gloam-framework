package com.gloamframework.cache.properties;

import com.gloamframework.cache.redis.properties.RedisCacheProperties;
import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("gloam.cache")
@Data
public class CacheProperties {

    /**
     * 默认使用redis的方式进行缓存
     */
    @MappingConfigurationProperty("spring.cache.type")
    private CacheType type = CacheType.REDIS;

    /**
     * 缓存名字
     */
    @MappingConfigurationProperty("spring.cache.cache-names")
    private List<String> cacheNames = new ArrayList<>();

    /**
     * redis缓存配置
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("spring.cache.redis")
    private RedisCacheProperties redis = new RedisCacheProperties();

}
