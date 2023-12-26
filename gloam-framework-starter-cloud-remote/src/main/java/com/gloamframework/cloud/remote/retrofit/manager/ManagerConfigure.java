package com.gloamframework.cloud.remote.retrofit.manager;

import com.gloamframework.cloud.remote.retrofit.manager.adapter.GloamCallAdapterFactory;
import com.gloamframework.cloud.remote.retrofit.manager.assembler.OkHttpClientAssembler;
import com.gloamframework.cloud.remote.retrofit.manager.assembler.RetrofitAssembler;
import com.gloamframework.cloud.remote.retrofit.manager.converter.convert.HttpResponseJsonConverter;
import com.gloamframework.cloud.remote.retrofit.manager.converter.convert.HttpResponseStreamConverter;
import com.gloamframework.cloud.remote.retrofit.peoperties.HttpProperties;
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
     * client配置：默认提供通过配置文件进行配置，外部可自定义
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
     * client：配置默认的拦截器
     */
    @Bean
    public OkHttpClientAssembler okHttpClientInterceptorAssembler() {
        return builder -> {
            // e.g.默认拦截器配置
            return builder;
        };
    }

    /**
     * 配置Retrofit转换器，默认支持基本数据类型，以及json
     */
    @Bean
    public RetrofitAssembler retrofitConvertAssembler() {
        // e.g.默认配置
        return builder -> builder.addCallAdapterFactory(GloamCallAdapterFactory.getFactory());
    }

    /**
     * json支持的转换器
     */
    @Bean
    public HttpResponseJsonConverter httpJsonConverter() {
        return new HttpResponseJsonConverter();
    }

    @Bean
    public HttpResponseStreamConverter httpStreamConverter() {
        return new HttpResponseStreamConverter();
    }
}
