package com.gloamframework.cloud.remote.retrofit.manager.converter;

import cn.hutool.core.util.ArrayUtil;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author 晓龙
 */
public interface GloamHttpResponseConverter<T> {

    /**
     * 该转换器支持的类型
     */
    MediaType[] contentTypeSupport();

    T convert(ResponseBody responseBody, Type type) throws IOException;

    static MediaType[] parse(String... contentTypes) {
        MediaType[] mediaTypes = {};
        for (String contentType : contentTypes) {
            mediaTypes = ArrayUtil.append(mediaTypes, MediaType.parse(contentType));
        }
        return mediaTypes;
    }
}
