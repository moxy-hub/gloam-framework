package com.gloamframework.web.response;

import com.gloamframework.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * gloam内部的结果解析，主要场景可用于feign等服务间调用
 *
 * @author 晓龙
 */
@Slf4j
public class GloamResultParser {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page<D> {
        private long pageNum;
        private long pageSize;
        private long total;
        private Collection<D> records;
    }

    /**
     * 解析list,在结果正确的情况下返回集合
     */
    public static <T> Collection<T> parse(WebList<T> webList) {
        validSuccess(webList);
        return webList.getData();
    }

    /**
     * 返回集合，并确保集合不为空
     */
    public static <T> Collection<T> parseNonNull(WebList<T> webList, String message) {
        Collection<T> parse = parse(webList);
        Assert.notEmpty(parse, message);
        return parse;
    }

    public static <T> Collection<T> parseNonNull(WebList<T> webList, Supplier<String> messageSupplier) {
        return parseNonNull(webList, nullSafeGet(messageSupplier));
    }

    public static <T> Collection<T> parseNonNull(WebList<T> webList) {
        return parseNonNull(webList, (Supplier<String>) null);
    }

    public static <T> Page<T> parse(WebPage<T> webPage) {
        validSuccess(webPage);
        return new Page<>(webPage.getPageNum(), webPage.getPageSize(), webPage.getTotal(), webPage.getData());
    }

    public static <T> Page<T> parseNonNull(WebPage<T> webPage, String message) {
        Page<T> parse = parse(webPage);
        Assert.notEmpty(parse.getRecords(), message);
        return parse;
    }

    public static <T> Page<T> parseNonNull(WebPage<T> webPage, Supplier<String> messageSupplier) {
        return parseNonNull(webPage, nullSafeGet(messageSupplier));
    }

    public static <T> Page<T> parseNonNull(WebPage<T> webPage) {
        return parseNonNull(webPage, (Supplier<String>) null);
    }

    public static <T> T parse(WebResult<T> webResult) {
        validSuccess(webResult);
        return webResult.getData();
    }

    public static <T> T parseNonNull(WebResult<T> webResult, String message) {
        T parse = parse(webResult);
        Assert.notNull(parse, message);
        return parse;
    }

    public static <T> T parseNonNull(WebResult<T> webResult, Supplier<String> messageSupplier) {
        return parseNonNull(webResult, nullSafeGet(messageSupplier));
    }

    public static <T> T parseNonNull(WebResult<T> webResult) {
        return parseNonNull(webResult, (Supplier<String>) null);
    }

    private static void validSuccess(Result<?> result) {
        Assert.notNull(result, () -> {
            log.error("[Result解析]:结果为null");
            return "响应结果对象为空";
        });
        Assert.isTrue(result.isSuccess(), () -> {
            log.error("[Result解析]:结果不成功,status:{},message:{}", result.getStatus(), result.getMessage());
            return result.getMessage();
        });
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : "响应结果对象为空");
    }
}
