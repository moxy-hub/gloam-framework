package com.gloamframework.cache.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
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

    @MappingConfigurationProperty("spring.cache.cache-names")
    private List<String> cacheNames = new ArrayList<>();

    @NestedConfigurationProperty
    @MappingConfigurationProperty("spring.cache.redis")
    private Redis redis = new Redis();

    @Data
    public static class Redis {
        @MappingConfigurationProperty("time-to-live")
        private Duration timeToLive;

        @MappingConfigurationProperty("cache-null-values")
        private boolean cacheNullValues = true;

        @MappingConfigurationProperty("key-prefix")
        private String keyPrefix = "Gloam_Cache_";

        @MappingConfigurationProperty("use-key-prefix")
        private boolean useKeyPrefix = true;
    }
}
