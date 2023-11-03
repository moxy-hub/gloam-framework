package com.gloamframework.core.http.manager;

import com.gloamframework.core.http.manager.assembler.OkHttpClientAssembler;
import com.gloamframework.core.http.manager.exception.OkHttpClientException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Map;

/**
 * OkHttpClient管理
 * <p>
 * 默认系统提供一个
 * 如果外部配置了，则使用外部的
 * 在使用拦截器时，将会对每个WebService创建client，如果外部统一配置，则内置拦截器失效
 * </p>
 *
 * @author 晓龙
 */
@Slf4j
public class OkHttpClientManager {

    private final ListableBeanFactory beanFactory;
    private final OkHttpClient DEFAULT_CLIENT;

    public OkHttpClientManager(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        // 实例系统默认的httpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 获取装配类
        Map<String, OkHttpClientAssembler> assemblerMap = beanFactory.getBeansOfType(OkHttpClientAssembler.class);
        for (OkHttpClientAssembler assembler : assemblerMap.values()) {
            builder = assembler.assemble(builder);
            if (builder == null) {
                throw new OkHttpClientException("在装配器{}中,builder返回值为null", "创建okhttpClient失败", assembler);
            }
        }
        DEFAULT_CLIENT = builder.build();
        log.debug("create default okhttp client:{} with connectTimeout:[{}] readTimeout:[{}] writeTimeout:[{}]", DEFAULT_CLIENT, DEFAULT_CLIENT.connectTimeoutMillis(), DEFAULT_CLIENT.readTimeoutMillis(), DEFAULT_CLIENT.writeTimeoutMillis());
    }

    public OkHttpClient obtainClient() {
        // 先去拿spring中，如果没有，则使用默认
        try {
            return beanFactory.getBean(OkHttpClient.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.debug("can not find okhttp client in spring ioc,fallback to use default");
            return DEFAULT_CLIENT;
        }
    }
}
