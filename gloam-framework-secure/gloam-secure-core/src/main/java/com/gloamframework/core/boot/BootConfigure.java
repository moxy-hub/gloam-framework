package com.gloamframework.core.boot;

import com.gloamframework.core.boot.context.SpringContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动aop代理
 */
@Configurable
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class BootConfigure {

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

}
