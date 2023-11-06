package com.gloamframework.core.http.manager;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.http.annotation.WebService;
import com.gloamframework.core.http.manager.assembler.OkHttpClientAssembler;
import com.gloamframework.core.http.manager.exception.OkHttpClientException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
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
        DEFAULT_CLIENT = this.obtainConfigureClientBuilder().build();
        log.debug("create default okhttp client:{} with connectTimeout:[{}] readTimeout:[{}] writeTimeout:[{}]", DEFAULT_CLIENT, DEFAULT_CLIENT.connectTimeoutMillis(), DEFAULT_CLIENT.readTimeoutMillis(), DEFAULT_CLIENT.writeTimeoutMillis());
    }

    public OkHttpClient obtainClient(WebService webService) {
        OkHttpClient httpClient;
        // 判断是否为单利默认
        if (webService.singletonClient()) {
            // 先去拿spring中，如果没有，则使用默认
            try {
                if (StrUtil.isNotBlank(webService.clientName())) {
                    httpClient = beanFactory.getBean(webService.clientName(), OkHttpClient.class);
                } else {
                    httpClient = beanFactory.getBean(OkHttpClient.class);
                }
            } catch (NoSuchBeanDefinitionException e) {
                log.debug("can not find okhttp client in spring ioc,fallback to use default");
                httpClient = DEFAULT_CLIENT;
            }
        } else {
            httpClient = this.obtainConfigureClientBuilder().build();
        }
        // 检查httpClient
        if (httpClient == null) {
            throw new OkHttpClientException("创建OkHttpClient失败，请检查WebService配置");
        }
        // 添加拦截器以及定制化的builder
        if (ArrayUtil.isAllEmpty(webService.clientInterceptors(), webService.clientAssembler())) {
            return httpClient;
        }
        // 创建新的client builder
        OkHttpClient.Builder clientBuilder = httpClient.newBuilder();
        // 添加拦截器
        for (Class<Interceptor> interceptor : webService.clientInterceptors()) {
            clientBuilder.addInterceptor(beanFactory.getBean(interceptor));
        }
        // builder
        for (Class<OkHttpClientAssembler> clientAssemblerClass : webService.clientAssembler()) {
            OkHttpClientAssembler clientAssembler = beanFactory.getBean(clientAssemblerClass);
            clientBuilder = clientAssembler.assemble(clientBuilder);
            if (clientBuilder == null) {
                throw new OkHttpClientException("在装配器{}中,builder返回值为null", "创建okhttpClient失败", clientAssembler);
            }
        }
        return clientBuilder.build();
    }

    private OkHttpClient.Builder obtainConfigureClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 获取装配类
        Map<String, OkHttpClientAssembler> assemblerMap = beanFactory.getBeansOfType(OkHttpClientAssembler.class);
        for (OkHttpClientAssembler assembler : assemblerMap.values()) {
            builder = assembler.assemble(builder);
            if (builder == null) {
                throw new OkHttpClientException("在装配器{}中,builder返回值为null", "创建okhttpClient失败", assembler);
            }
        }
        return builder;
    }
}
