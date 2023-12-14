package com.gloamframework.web.security.envelope;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 晓龙
 */
@Configurable
public class WebEnvelopeConfigure {

    @Bean
    public WebEnvelopeFilter webEnvelopeFilter(){
        return new WebEnvelopeFilter();
    }

}
