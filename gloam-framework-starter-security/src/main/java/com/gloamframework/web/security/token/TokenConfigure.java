package com.gloamframework.web.security.token;

import com.gloamframework.web.security.properties.SecurityProperties;
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
    @ConditionalOnMissingBean(TokenPreHandlerFilter.class)
    public TokenPreHandlerFilter tokenPreHandlerFilter(SecurityProperties securityProperties) {
        return new TokenPreHandlerFilter(securityProperties.getToken());
    }

    @Bean
    @ConditionalOnMissingBean(DeviceMatchFilter.class)
    public DeviceMatchFilter deviceMatchFilter() {
        return new DeviceMatchFilter();
    }

}
