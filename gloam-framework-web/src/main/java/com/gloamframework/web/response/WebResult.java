package com.gloamframework.web.response;

import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;

/**
 * 统一后端响应结果类
 *
 * @author 晓龙
 */
@ApiModel(description = "单对象类型响应对象")
public class WebResult<T> extends Result<T> {

    private WebResult(T data, Integer status, boolean success, String message, Object... params) {
        super(data, status, success, message, params);
    }

    /**
     * 请求成功
     *
     * @param data    传输数据
     * @param message 传输消息
     */
    public static <T> WebResult<T> success(T data, String message, Object... params) {
        return new WebResult<>(data, HttpStatus.OK.value(), true, message, params);
    }

    public static <T> WebResult<T> success(T data) {
        return success(data, null);
    }

    public static <T> WebResult<T> success() {
        return success(null);
    }

    public static WebResult<Void> successWithMessage(String message, Object... params) {
        return success(null, message, params);
    }

    /**
     * 请求失败，返回的状态是200，表示请求后端成功处理，但是业务不支持
     *
     * @param message 传输消息
     */
    public static <T> WebResult<T> fail(String message, Object... params) {
        return new WebResult<>(null, HttpStatus.OK.value(), false, message, params);
    }

    public static <T> WebResult<T> fail() {
        return WebResult.fail(null);
    }

    /**
     * 拒绝请求，状态为500
     */
    public static <T> WebResult<T> refuse(String message, Object... params) {
        return new WebResult<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), false, message, params);
    }

    /**
     * 未授权请求，状态为401
     */
    public static <T> WebResult<T> unAuthorization(String message, Object... params) {
        return new WebResult<>(null, HttpStatus.UNAUTHORIZED.value(), false, message, params);
    }
}
