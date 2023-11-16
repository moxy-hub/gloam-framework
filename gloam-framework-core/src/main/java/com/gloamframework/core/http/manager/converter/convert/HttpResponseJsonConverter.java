package com.gloamframework.core.http.manager.converter.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.gloamframework.core.http.manager.converter.GloamHttpResponseConverter;
import com.gloamframework.core.http.manager.converter.type.GloamType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Json支持的转换器
 *
 * @author 晓龙
 */
@Slf4j
public class HttpResponseJsonConverter implements GloamHttpResponseConverter<Object> {

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
        String originalBody = responseBody.string();
        log.debug("请求结果--> {}", originalBody);
        try {
            return JSON.parseObject(originalBody, type, CAMEL_CASE_PARSER);
        } catch (JSONException jsonException) {
            log.warn("响应解析json失败,具体内容为:{},将返回字符串处理，如果处理类不是String类型将返回null", originalBody, jsonException);
            if (new GloamType(type).is(String.class)) {
                return originalBody;
            } else {
                return null;
            }
        }

    }
}
