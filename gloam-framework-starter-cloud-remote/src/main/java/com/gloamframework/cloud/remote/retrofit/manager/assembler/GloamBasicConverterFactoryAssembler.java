package com.gloamframework.cloud.remote.retrofit.manager.assembler;

import com.gloamframework.cloud.remote.retrofit.manager.converter.GloamBasicConverterFactory;

/**
 * gloam http转换器配置
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface GloamBasicConverterFactoryAssembler {

    /**
     * 配置gloam支持的转换器，该转换器为gloam支持，如果需要配置Retrofit的转换器，请关注
     *
     * @see RetrofitAssembler
     */
    GloamBasicConverterFactory.Builder assemble(GloamBasicConverterFactory.Builder builder);

}
