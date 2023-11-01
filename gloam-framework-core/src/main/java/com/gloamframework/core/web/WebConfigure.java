package com.gloamframework.core.web;

import com.gloamframework.core.web.exception.GlomaHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configurable
public class WebConfigure {

    @Bean
    @ConditionalOnMissingBean(GlomaHandlerExceptionResolver.class)
    public HandlerExceptionResolver gloamHandlerExceptionResolver() {
        return new GlomaHandlerExceptionResolver();
    }

}
