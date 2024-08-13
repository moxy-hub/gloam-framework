package com.gloamframework.web.debouncing;

import com.gloamframework.web.security.GloamSecurityCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 * 防抖配置类
 */
@Configurable
@Slf4j
public class DebouncingConfigure {

    public DebouncingConfigure() {
        log.info("[Debouncing]:启动接口防抖保护，请在需要保护的接口上标注注解@Debouncing");
    }

    @Bean
    private DebouncingMatcher debouncingMatcher() {
        return new DebouncingMatcher();
    }

    @Bean
    @ConditionalOnBean(DebouncingMatcher.class)
    public DebouncingFilter debouncingFilter(GloamSecurityCacheManager cacheManager) {
        return new DebouncingFilter(debouncingMatcher(), cacheManager);
    }
}
