package com.gloamframework.web.security.token;

import com.gloamframework.core.boot.diagnostics.GloamStartException;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.properties.SecurityProperties;
import com.gloamframework.web.security.token.properties.TokenProperties;
import com.gloamframework.web.security.token.strategy.JwtTokenManager;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * token spring 配置类
 *
 * @author 晓龙
 */
@Configurable
@ComponentScan("com.gloamframework.web.security.token.exception")
public class TokenConfigure {

    @Bean
    @SuppressWarnings("all")
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(SecurityProperties securityProperties, GloamSecurityCacheManager cacheManager) {
        TokenProperties tokenProperties = securityProperties.getToken();
        // 通过策略进行注入
        switch (tokenProperties.getStrategy()) {
            case JWT: {
                return new JwtTokenManager(tokenProperties, cacheManager);
            }
            default: {
                throw new GloamStartException("不支持的Token策略");
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean(DeviceAndTokenStrategyMatchFilter.class)
    public DeviceAndTokenStrategyMatchFilter deviceMatchFilter() {
        return new DeviceAndTokenStrategyMatchFilter();
    }

    @Bean
    @ConditionalOnMissingBean(TokenPreHandlerFilter.class)
    public TokenPreHandlerFilter tokenPreHandlerFilter(SecurityProperties securityProperties) {
        return new TokenPreHandlerFilter(securityProperties.getToken());
    }

    @Bean
    @ConditionalOnMissingBean(TokenAuthenticationFilter.class)
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenManager tokenManager) {
        return new TokenAuthenticationFilter(tokenManager);
    }

}
