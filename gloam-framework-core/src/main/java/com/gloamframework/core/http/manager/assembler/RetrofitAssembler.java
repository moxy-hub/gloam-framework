package com.gloamframework.core.http.manager.assembler;

import retrofit2.Retrofit;

/**
 * 用于装配Retrofit
 */
@FunctionalInterface
public interface RetrofitAssembler {


    /**
     * 实现该方法，将会在创建默认的Retrofit管理器时获取到全部的装配器，为每个webService进行装配
     */
    Retrofit.Builder assemble(Retrofit.Builder builder);

}
