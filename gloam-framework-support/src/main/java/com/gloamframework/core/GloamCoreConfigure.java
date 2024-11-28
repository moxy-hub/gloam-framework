package com.gloamframework.core;

import com.gloamframework.core.boot.context.GloamContext;
import com.gloamframework.core.json.JsonConfigure;
import com.gloamframework.core.logging.properties.LoggingProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Gloam 核心环境配置，spring自动装配
 *
 * @author 晓龙
 */
@Configurable
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableConfigurationProperties(LoggingProperties.class)
@Import(JsonConfigure.class)
public class GloamCoreConfigure {

    @Bean
    public GloamContext gloamContext() {
        return new GloamContext();
    }

}
