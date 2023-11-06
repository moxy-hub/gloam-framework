package com.gloamframework.core.http.manager.converter.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.gloamframework.core.http.manager.converter.GloamHttpResponseConverter;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Json支持的转换器
 *
 * @author 晓龙
 */
public class HttpJsonConverter implements GloamHttpResponseConverter<Object> {

    protected static final ParserConfig CAMEL_CASE_PARSER = new ParserConfig();

    private static final MediaType[] supportMediaType;

    static {
        // 统一json解析，且下划线驼峰化
        CAMEL_CASE_PARSER.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        supportMediaType = GloamHttpResponseConverter.parse("application/json", "application/json;charset=UTF-8");
    }

    @Override
    public MediaType[] contentTypeSupport() {
        return supportMediaType;
    }

    @Override
    public Object convert(ResponseBody responseBody, Type type) throws IOException {
        return JSON.parseObject(responseBody.string(), type, CAMEL_CASE_PARSER);
    }
}
