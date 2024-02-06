package com.gloamframework.web.response;

import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 晓龙
 */
@ApiModel(description = "LIST类型响应对象")
public class WebList<T> extends Result<Collection<T>> {

    private WebList(Collection<T> data, Integer status, boolean success, String message, Object... params) {
        super(data, status, success, message, params);
    }

    /**
     * 请求成功
     *
     * @param data    传输数据
     * @param message 传输消息
     */
    public static <T> WebList<T> success(Collection<T> data, String message, Object... params) {
        return new WebList<>(data, HttpStatus.OK.value(), true, message, params);
    }

    public static <T> WebList<T> success(Collection<T> data) {
        return success(data, null);
    }

    public static <T> WebList<T> success() {
        return success(new ArrayList<>());
    }

    /**
     * 请求失败，返回的状态是200，表示请求后端成功处理，但是业务不支持
     *
     * @param message 传输消息
     */
    public static <T> WebList<T> fail(String message, Object... params) {
        return new WebList<>(new ArrayList<>(), HttpStatus.OK.value(), false, message, params);
    }

    public static <T> WebList<T> fail() {
        return WebList.fail(null);
    }

}
