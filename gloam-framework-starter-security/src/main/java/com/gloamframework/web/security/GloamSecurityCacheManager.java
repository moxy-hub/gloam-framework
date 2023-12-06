package com.gloamframework.web.security;

import lombok.Getter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author 晓龙
 */
@Getter
public final class GloamSecurityCacheManager {

    private static final String DEFAULT_CACHE_NAME = "Security";

    private final Cache cache;

    public GloamSecurityCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(DEFAULT_CACHE_NAME);
    }

}
