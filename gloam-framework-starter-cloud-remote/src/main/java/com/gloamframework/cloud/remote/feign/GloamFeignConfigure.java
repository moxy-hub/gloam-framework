package com.gloamframework.cloud.remote.feign;

import com.gloamframework.cloud.remote.feign.interceptor.GloamErrorResponseInterceptor;
import com.gloamframework.cloud.remote.feign.interceptor.GloamRequestInterceptor;
import com.gloamframework.cloud.remote.feign.interceptor.GloamResponseInterceptor;
import com.gloamframework.cloud.remote.feign.properties.GloamFeignProperties;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author 晓龙
 */
@Configurable
@ConditionalOnClass(name = "feign.Feign")
@EnableConfigurationProperties(GloamFeignProperties.class)
public class GloamFeignConfigure {

    @Bean
    public RequestInterceptor gloamRequestInterceptor() {
        return new GloamRequestInterceptor();
    }

    @Bean
    public Decoder gloamResponseInterceptor(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new GloamResponseInterceptor(messageConverters);
    }

    @Bean
    public ErrorDecoder gloamErrorResponseInterceptor() {
        return new GloamErrorResponseInterceptor();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
