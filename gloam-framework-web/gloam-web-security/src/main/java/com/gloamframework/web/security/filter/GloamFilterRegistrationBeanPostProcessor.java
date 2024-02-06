package com.gloamframework.web.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * gloam过滤器前置处理器，防止过滤器进入spring ioc中后自动进行注册
 *
 * @author 晓龙
 */
@Slf4j
public class GloamFilterRegistrationBeanPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        //找到所有自定义的GloamFilter，并注册FilterRegistrationBean
        String[] beanNames = beanFactory.getBeanNamesForType(GloamOncePerRequestFilter.class);
        for (String beanName : beanNames) {
            this.registerFilterRegistrationBean(defaultListableBeanFactory, beanName);
        }
    }

    private void registerFilterRegistrationBean(DefaultListableBeanFactory beanFactory, String beanName) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(FilterRegistrationBean.class);
        beanDefinitionBuilder.addPropertyReference("filter", beanName);
        beanDefinitionBuilder.addPropertyValue("enabled", false);
        beanFactory.registerBeanDefinition(beanName + "FilterRegistrationBean",
                beanDefinitionBuilder.getRawBeanDefinition());
        log.info("Gloam filter {} disabled register to spring ioc for spring security", beanName);
    }

}
