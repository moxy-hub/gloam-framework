package com.gloamframework.core;

import com.gloamframework.core.context.SpringContext;
import com.gloamframework.core.logging.properties.LoggingProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Gloam 核心环境配置，spring自动装配
 *
 * @author 晓龙
 */
@Configurable
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableConfigurationProperties(LoggingProperties.class)
public class GloamCoreConfigure {

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

}
