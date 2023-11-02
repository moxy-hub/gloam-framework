package com.gloamframework.core.web.undertow;

import com.gloamframework.core.web.undertow.properties.UndertowProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * undertow配置注册
 * @author 晓龙
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(UndertowProperties.class)
@Configurable
public class UndertowConfigure {

    @Bean
    public BufferPoolCustomizer bufferPoolCustomizer(UndertowProperties properties){
        return new BufferPoolCustomizer(properties.getBufferPool());
    }

}
