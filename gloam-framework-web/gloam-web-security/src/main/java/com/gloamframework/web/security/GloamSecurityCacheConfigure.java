package com.gloamframework.web.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 */
@Configurable
public class GloamSecurityCacheConfigure {

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public GloamSecurityCacheManager gloamSecurityCacheManager(CacheManager cacheManager) {
        return new GloamSecurityCacheManager(cacheManager);
    }

}
