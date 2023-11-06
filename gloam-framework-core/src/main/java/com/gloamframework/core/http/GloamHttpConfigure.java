package com.gloamframework.core.http;

import com.gloamframework.core.http.bean.HttpRetrofitBeanDefinitionRegistrar;
import com.gloamframework.core.http.inject.WebServiceInjectAnnotationBeanPostProcessor;
import com.gloamframework.core.http.manager.ManagerConfigure;
import com.gloamframework.core.http.peoperties.HttpProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * http服务配置类
 *
 * @author 晓龙
 */
@Configurable
@Import({HttpRetrofitBeanDefinitionRegistrar.class, ManagerConfigure.class})
@EnableConfigurationProperties(HttpProperties.class)
public class GloamHttpConfigure {

    /**
     * 注解@WebServiceInject实现
     */
    @Bean
    public WebServiceInjectAnnotationBeanPostProcessor webServiceInjectAnnotationBeanPostProcessor() {
        return new WebServiceInjectAnnotationBeanPostProcessor();
    }

}
