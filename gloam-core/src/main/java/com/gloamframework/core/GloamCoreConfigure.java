package com.gloamframework.core;

import com.gloamframework.core.context.SpringContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Gloam 核心环境配置，spring自动装配
 *
 * @author 晓龙
 */
@Configurable
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class GloamCoreConfigure {

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

}
