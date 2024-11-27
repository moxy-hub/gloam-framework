package com.gloamframework.core.boot;

import com.gloamframework.common.error.GloamNonSupportedFunctionException;
import com.gloamframework.core.lang.exception.GloamIllegalArgumentException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * gloam全局上下文，用于各模块对于单例对象｜数据的获取
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月27日 09:44
 */
public class GloamContext {

    private static final Map<String, Object> CONTENT_BEANS = new ConcurrentHashMap<>();

    public static <T> void put(GloamObjectKey<T> key, T bean) {
        if (Objects.isNull(key) || Objects.isNull(bean)) {
            throw new GloamNonSupportedFunctionException("[GloamContext]:Gloam上下文不支持放入的key或bean为空");
        }
        CONTENT_BEANS.put(key.getKey(), bean);
    }

    public static <T> T get(GloamObjectKey<T> key) {
        if (Objects.isNull(key)) {
            throw new GloamNonSupportedFunctionException("[GloamContext]:Gloam上下文查询的key不能为null");
        }
        Object bean = CONTENT_BEANS.get(key.getKey());
        if (!key.canTransform(bean)) {
            throw new GloamIllegalArgumentException("[GloamContext]:Key-{}存储的对象不一致与定义的类型不一致", key.getKey());
        }
        return key.transform(bean);
    }

}
