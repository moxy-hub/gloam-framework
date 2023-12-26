package com.gloamframework.cloud.remote.retrofit.manager;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.cloud.remote.retrofit.annotation.WebService;
import com.gloamframework.cloud.remote.retrofit.manager.assembler.RetrofitAssembler;
import com.gloamframework.cloud.remote.retrofit.manager.converter.GloamBasicConverterFactory;
import com.gloamframework.cloud.remote.retrofit.manager.converter.GloamHttpResponseConverter;
import com.gloamframework.cloud.remote.retrofit.manager.exception.RetrofitException;
import org.springframework.beans.factory.ListableBeanFactory;
import retrofit2.CallAdapter;
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
    public Retrofit obtainRetrofit(String renderURL, WebService webService) {
        // 先解析地址
        if (StrUtil.isBlank(renderURL)) {
            throw new RetrofitException("请在@WebService注解中配置远程连接路径", "Retrofit配置失败");
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        // 装配器先进行配置
        for (RetrofitAssembler assembler : assemblers) {
            builder = assembler.assemble(builder);
            if (builder == null) {
                throw new RetrofitException("在装配器{}中,builder返回值为null", "创建retrofit失败", assembler);
            }
        }
        // 进行默认的配置
        return webServiceBuild(renderURL, webService, builder).build();
    }

    @SuppressWarnings("all")
    private Retrofit.Builder webServiceBuild(String renderURL, WebService webService, Retrofit.Builder builder) {

        // 配置默认的gloam支持转换器
        GloamBasicConverterFactory.Builder converterBuilder = new GloamBasicConverterFactory.Builder();
        for (Class<? extends GloamHttpResponseConverter> converter : webService.converters()) {
            converterBuilder.addHttpConverter(beanFactory.getBean(converter));
        }
        if (ArrayUtil.isNotEmpty(webService.callAdapterFactories())) {
            for (Class<? extends CallAdapter.Factory> callAdapterFactory : webService.callAdapterFactories()) {
                builder.addCallAdapterFactory(beanFactory.getBean(callAdapterFactory));
            }
        }
        return builder.baseUrl(renderURL)
                .addConverterFactory(converterBuilder.build())
                // OKHttpClient
                .client(httpClientManager.obtainClient(webService));
    }
}
