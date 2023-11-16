/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.gloamframework.core.http.manager.adapter;

import com.gloamframework.core.http.manager.adapter.exception.GloamHttpCallException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author 晓龙
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class GloamCallAdapterFactory extends CallAdapter.Factory {

    private static final GloamCallAdapterFactory bodyCallAdapterFactory = new GloamCallAdapterFactory();

    public static GloamCallAdapterFactory getFactory() {
        return bodyCallAdapterFactory;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (Call.class.isAssignableFrom(getRawType(returnType))) {
            return null;
        }
        if (CompletableFuture.class.isAssignableFrom(getRawType(returnType))) {
            return null;
        }
        if (Response.class.isAssignableFrom(getRawType(returnType))) {
            return null;
        }
        return new BodyCallAdapter<>(returnType, annotations, retrofit);
    }

    static final class BodyCallAdapter<R> implements CallAdapter<R, R> {

        private final Type returnType;

        private final Retrofit retrofit;

        private final Annotation[] annotations;

        BodyCallAdapter(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            this.returnType = returnType;
            this.retrofit = retrofit;
            this.annotations = annotations;
        }

        @Override
        public Type responseType() {
            return returnType;
        }

        @Override
        public R adapt(Call<R> call) {
            Response<R> response;
            Request request = call.request();
            log.info("发起请求:{}#{}", request.method(), request.url());
            try {
                response = call.execute();
            } catch (IOException e) {
                throw new GloamHttpCallException("http请求失败", e);
            }
            if (response.isSuccessful()) {
                return response.body();
            }
            // 请求失败，不是200
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) {
                return null;
            }
            try {
                log.error("请求:{}#{} 响应code:{},响应message:{}", request.method(), request.url(), response.code(), response.message());
                Converter<ResponseBody, R> converter = retrofit.responseBodyConverter(responseType(), annotations);
                return converter.convert(Objects.requireNonNull(errorBody));
            } catch (IOException e) {
                throw new GloamHttpCallException("http响应错误消息转换失败", e);
            }

        }
    }
}
