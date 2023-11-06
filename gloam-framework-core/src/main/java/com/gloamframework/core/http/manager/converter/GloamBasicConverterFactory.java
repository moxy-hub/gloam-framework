package com.gloamframework.core.http.manager.converter;

import com.alibaba.fastjson.JSON;
import com.gloamframework.core.http.manager.converter.type.GloamType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Retrofit基础转换器
 *
 * @author 晓龙
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class GloamBasicConverterFactory extends Converter.Factory {

    private final Set<GloamHttpResponseConverter<Object>> gloamHttpResponseConverters;

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        // 通过类型判断，创建合适的转换器
        GloamType gloamType = new GloamType(type);
        if (gloamType.is(String.class)) {
            return (Converter<ResponseBody, String>) ResponseBody::string;
        } else if (gloamType.is(Integer.class)) {
            return (Converter<ResponseBody, Integer>) res -> Integer.valueOf(res.string());
        } else if (gloamType.is(Long.class)) {
            return (Converter<ResponseBody, Long>) res -> Long.valueOf(res.string());
        } else if (gloamType.is(Boolean.class)) {
            return (Converter<ResponseBody, Boolean>) res -> Boolean.valueOf(res.string());
        } else if (gloamType.is(Float.class)) {
            return (Converter<ResponseBody, Float>) res -> Float.valueOf(res.string());
        } else if (gloamType.is(Double.class)) {
            return (Converter<ResponseBody, Double>) res -> Double.valueOf(res.string());
        }
        // 获取gloam格式的转换器进行处理
        return new GloamContentTypeConverter<>(type, gloamHttpResponseConverters);
    }

    /**
     * 通过文本类型进行判断分发
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class GloamContentTypeConverter<T> implements Converter<ResponseBody, T> {

        private final Type type;
        private final Set<GloamHttpResponseConverter<T>> gloamHttpResponseConverters;

        @Override
        @SuppressWarnings("all")
        public T convert(ResponseBody responseBody) throws IOException {
            MediaType currentMediaType = responseBody.contentType();
            // 获取全部的转换器
            if (gloamHttpResponseConverters != null) {
                for (GloamHttpResponseConverter<T> converter : gloamHttpResponseConverters) {
                    if (Arrays.asList(converter.contentTypeSupport()).contains(currentMediaType)) {
                        log.debug("http response converter switch to {}", converter);
                        return converter.convert(responseBody, type);
                    }
                }
            }
            // 使用默认
            log.debug("unfound gloam http response converter set,fallback to use default json parser");
            return JSON.parseObject(responseBody.string(), type);
        }

    }

    public static class Builder {

        private static final Set<GloamHttpResponseConverter<Object>> gloamHttpResponseConverters = new HashSet<>();

        public Builder addHttpConverter(GloamHttpResponseConverter<Object> gloamHttpResponseConverter) {
            gloamHttpResponseConverters.add(gloamHttpResponseConverter);
            return this;
        }

        public GloamBasicConverterFactory build() {
            return new GloamBasicConverterFactory(gloamHttpResponseConverters);
        }
    }

}
