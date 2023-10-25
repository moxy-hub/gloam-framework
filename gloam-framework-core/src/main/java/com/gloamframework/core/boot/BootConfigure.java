package com.gloamframework.core.boot;

import com.gloamframework.core.boot.context.SpringContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class BootConfigure {

    @Bean
    public SpringContext springContext() {
        return new SpringContext();
    }

}
