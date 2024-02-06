package com.gloamframework.cloud.remote.feign.interceptor;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gloamframework.cloud.remote.feign.exception.FeignException;
import com.gloamframework.cloud.remote.feign.util.GzipUtil;
import com.gloamframework.common.error.GloamInternalException;
import com.gloamframework.web.response.Result;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * gloam的feign错误处理
 *
 * @author 晓龙
 */
@Slf4j
public class GloamErrorResponseInterceptor implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        response = GzipUtil.decode(response);
        String body;
        try {
            body = new String(IoUtil.readBytes(response.body().asInputStream()));
        } catch (IOException e) {
            throw new GloamInternalException("feign调用错误:读取响应body失败", e);
        }
        log.error("feign服务调用失败,原因:{}", body);
        String message;
        try {
            // 默认解析内置的Result
            Result<?> result = JSON.parseObject(body, Result.class);
            if (result == null) {
                throw new GloamInternalException("feign调用错误:解析结果为空,解析目标对象:{}", Result.class);
            }
            if (StrUtil.isBlank(result.getMessage())) {
                throw new GloamInternalException("feign调用错误:解析结果的消息为空,请检查提供方");
            }
            message = result.getMessage();
        } catch (Exception e) {
            log.error("解析结果异常", e);
            message = "内部服务暂时不可用,请稍后再试";
        }
        return new FeignException(message);
    }

}
