package com.gloamframework.cloud.remote.feign.interceptor;

import com.gloamframework.cloud.remote.feign.util.GzipUtil;
import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author 晓龙
 */
public class GloamResponseInterceptor extends SpringDecoder {

    public GloamResponseInterceptor(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        return super.decode(GzipUtil.decode(response), type);
    }
}
