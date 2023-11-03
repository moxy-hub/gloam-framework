package com.gloamframework.core.http.manager.assembler;

import okhttp3.OkHttpClient;

/**
 * 用于装配系统默认的okHttpClient
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface OkHttpClientAssembler {

    /**
     * 实现该方法，将会在创建默认的okhttpClient时进行回调，可以使用builder进行配置
     */
    OkHttpClient.Builder assemble(OkHttpClient.Builder builder);

}
