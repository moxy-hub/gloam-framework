package com.gloamframework.core.http;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author 晓龙
 */
public class GloamBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}
