package com.gloamframework.core.boot.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * spring上下文感知工具，项目启动会自行进行感知，使用时只需要静态点击即可
 */
@Slf4j
public class SpringContext {

    private static ApplicationContext applicationContext;

    @EventListener
    public void contextRefreshedEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        log.info("spring context is ready for use");
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

}
