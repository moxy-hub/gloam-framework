package com.gloamframework.cache.redis.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

import java.time.Duration;

/**
 * Redis缓存配置
 *
 * @author 晓龙
 */
@Data
public class RedisCacheProperties {

    @MappingConfigurationProperty("time-to-live")
    private Duration timeToLive;

    @MappingConfigurationProperty("cache-null-values")
    private boolean cacheNullValues = true;

    @MappingConfigurationProperty("key-prefix")
    private String keyPrefix = "Gloam_Cache_";

    @MappingConfigurationProperty("use-key-prefix")
    private boolean useKeyPrefix = true;

}
