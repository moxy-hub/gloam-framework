package com.gloamframework.web.security;

import com.gloamframework.cache.GloamCache;
import lombok.Getter;
import org.springframework.cache.CacheManager;

/**
 * @author 晓龙
 */
@Getter
public final class GloamSecurityCacheManager {

    private static final String DEFAULT_CACHE_NAME = "Security";

    private final GloamCache cache;

    public GloamSecurityCacheManager(CacheManager cacheManager) {
        this.cache = new GloamCache(cacheManager.getCache(DEFAULT_CACHE_NAME));
    }

}
