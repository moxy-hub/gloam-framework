package com.gloamframework.core.bean;

import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年08月06日 15:25
 */
public class GloamBeanConfigure {

    @Bean
    public GloamReplaceBeanProcessor gloamBeanDefinitionRegistryPostProcessor() {
        return new GloamReplaceBeanProcessor();
    }

}
