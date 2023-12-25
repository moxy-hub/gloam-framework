package com.gloamframework.web;

import com.gloamframework.web.exception.GloamValidationExceptionResolver;
import com.gloamframework.web.exception.GlomaHandlerExceptionResolver;
import com.gloamframework.web.properties.WebServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@Configurable
@ConditionalOnWebApplication
@EnableConfigurationProperties(WebServerProperties.class)
public class GloamWebConfigure {

    @Bean
    @ConditionalOnMissingBean(GlomaHandlerExceptionResolver.class)
    public GlomaHandlerExceptionResolver gloamHandlerExceptionResolver() {
        return new GlomaHandlerExceptionResolver();
    }

    @Bean
    @ConditionalOnMissingBean(GloamValidationExceptionResolver.class)
    public GloamValidationExceptionResolver gloamValidationExceptionResolver() {
        return new GloamValidationExceptionResolver();
    }
}
