package com.gloamframework.core.http.manager;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.http.annotation.WebService;
import com.gloamframework.core.http.manager.assembler.RetrofitAssembler;
import com.gloamframework.core.http.manager.exception.RetrofitException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ListableBeanFactory;
import retrofit2.Retrofit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * retrofit管理类
 * <p>
 * 该类提供通过@WebService注解获取管理器
 * </p>
 *
 * @author 晓龙
 */
public class RetrofitManager {

    private final OkHttpClientManager httpClientManager;
    private final Set<RetrofitAssembler> assemblers;
    private final ListableBeanFactory beanFactory;

    public RetrofitManager(ListableBeanFactory beanFactory, OkHttpClientManager httpClientManager) {
        this.beanFactory = beanFactory;
        this.httpClientManager = httpClientManager;
        // 获取全部的装配器
        Map<String, RetrofitAssembler> assemblerMap = beanFactory.getBeansOfType(RetrofitAssembler.class);
        this.assemblers = new HashSet<>(assemblerMap.values());
    }

    /**
     * 通过注解换取Retrofit
     */
    public Retrofit obtainRetrofit(WebService webService) {
        Retrofit.Builder builder = new Retrofit.Builder();
        // 装配器先进行配置
        for (RetrofitAssembler assembler : assemblers) {
            builder = assembler.assemble(builder);
            if (builder == null) {
                throw new RetrofitException("在装配器{}中,builder返回值为null", "创建retrofit失败", assembler);
            }
        }
        // 进行默认的配置
        return webServiceBuild(webService, builder).build();
    }

    private Retrofit.Builder webServiceBuild(WebService webService, Retrofit.Builder builder) {
        // 先解析地址
        if (StrUtil.isBlank(webService.remoteURL())) {
            throw new RetrofitException("请在@WebService注解中配置远程连接路径", "Retrofit配置失败");
        }
        // httpclient
        OkHttpClient okHttpClient = httpClientManager.obtainClient();
        if (ArrayUtil.isNotEmpty(webService.interceptors())) {
            OkHttpClient.Builder clientBuilder = okHttpClient.newBuilder();
            for (Class<Interceptor> interceptorClass : webService.interceptors()) {
                // 获取spring中注册的拦截器
                clientBuilder.addInterceptor(beanFactory.getBean(interceptorClass));
            }
            okHttpClient = clientBuilder.build();
        }
        return builder.baseUrl(webService.remoteURL())
                // OKHttpClient
                .client(okHttpClient);
    }
}
