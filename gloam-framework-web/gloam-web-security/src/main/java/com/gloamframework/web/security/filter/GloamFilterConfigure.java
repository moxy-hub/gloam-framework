package com.gloamframework.web.security.filter;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author 晓龙
 */
@Configurable
public class GloamFilterConfigure {

    @Bean
    public GloamFilterRegistrationBeanPostProcessor gloamFilterRegistrationBeanPostProcessor() {
        return new GloamFilterRegistrationBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(GloamFilterConfigurerAdapter.class)
    public GloamHttpSecurityConfigurerAdapter gloamFilterConfigurerAdapter(List<GloamOncePerRequestFilter> gloamOncePerRequestFilters) {
        return new GloamFilterConfigurerAdapter(gloamOncePerRequestFilters);
    }

}
