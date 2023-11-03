package com.gloamframework.core.http.manager;

import com.gloamframework.core.http.manager.assembler.OkHttpClientAssembler;
import com.gloamframework.core.http.peoperties.HttpProperties;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author 晓龙
 */
@Configurable
public class ManagerConfigure {

    @Bean
    public OkHttpClientManager okHttpClientManager(ListableBeanFactory beanFactory) {
        return new OkHttpClientManager(beanFactory);
    }

    @Bean
    public RetrofitManager retrofitManager(ListableBeanFactory beanFactory, OkHttpClientManager okHttpClientManager) {
        return new RetrofitManager(beanFactory, okHttpClientManager);
    }

    /**
     * 默认提供通过配置文件进行配置，外部可自定义
     */
    @Bean
    public OkHttpClientAssembler okHttpClientPropertiesAssembler(HttpProperties httpProperties) {
        return builder -> {
            HttpProperties.Client clientProperties = httpProperties.getClient();
            return builder
                    .connectTimeout(clientProperties.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(clientProperties.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(clientProperties.getWriteTimeout(), TimeUnit.SECONDS)
                    .callTimeout(clientProperties.getCallTimeout(), TimeUnit.SECONDS);
        };
    }

    /**
     * 配置默认的拦截器
     */
    @Bean
    public OkHttpClientAssembler okHttpClientInterceptorAssembler() {
        return builder -> {
            // todo:默认拦截器配置
            return builder;
        };
    }
}
