package com.gloamframework.test.core;

import com.gloamframework.core.bean.ReplaceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年08月06日 15:22
 */
@Configuration
public class TestConfiguration {

    @Bean
    public TestObject testObject() {
        return new TestObject("11");
    }

    @Bean
    @Primary
    public TestObject testObject11() {
        return new TestObject("00");
    }

    @ReplaceBean(targets = {"testObject","testObject11"})
    public TestObject testObject2() {
        return new TestObject("22");
    }

}
