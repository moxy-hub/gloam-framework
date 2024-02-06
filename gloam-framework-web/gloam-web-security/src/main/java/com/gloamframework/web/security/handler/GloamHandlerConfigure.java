package com.gloamframework.web.security.handler;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author 晓龙
 */
@Configurable
public class GloamHandlerConfigure {

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler gloamAccessDeniedHandler() {
        return new GloamAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint gloamAuthenticationEntryPoint() {
        return new GloamAuthenticationEntryPoint();
    }

}
