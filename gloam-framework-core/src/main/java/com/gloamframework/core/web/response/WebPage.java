package com.gloamframework.core.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回类
 *
 * @author 晓龙
 */
@Getter
@ApiModel(description = "PAGE类型响应对象")
public class WebPage<T> extends Result<List<T>> {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_TOTAL = 0;

    @ApiModelProperty(value = "分页页数")
    private final int pageNum;

    @ApiModelProperty(value = "分页大小")
    private final int pageSize;

    @ApiModelProperty(value = "分页总数")
    private final int total;

    private WebPage(List<T> data, HttpStatus status, boolean success, String message, int pageNum, int pageSize, int total, Object... params) {
        super(data, status, success, message, params);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * 请求成功
     *
     * @param data    传输数据
     * @param message 传输消息
     */
    public static <T> WebPage<T> success(List<T> data, int pageNum, int pageSize, int total, String message, Object... params) {
        return new WebPage<>(data, HttpStatus.OK, true, message, pageNum, pageSize, total, params);
    }

    public static <T> WebPage<T> success(List<T> data, int pageNum, int pageSize, int total) {
        return success(data, pageNum, pageSize, total, null);
    }

    public static <T> WebPage<T> success() {
        return success(new ArrayList<>(), DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_TOTAL);
    }

    /**
     * 请求失败，返回的状态是200，表示请求后端成功处理，但是业务不支持
     *
     * @param message 传输消息
     */
    public static <T> WebPage<T> fail(String message, Object... params) {
        return new WebPage<>(new ArrayList<>(), HttpStatus.OK, false, message, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_TOTAL, params);
    }

    public static <T> WebPage<T> fail() {
        return WebPage.fail(null);
    }

}
