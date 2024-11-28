package com.gloamframework.core.boot.context;

import com.gloamframework.common.error.GloamNonSupportedFunctionException;
import com.gloamframework.common.lang.StringUtil;
import com.gloamframework.core.lang.exception.GloamIllegalArgumentException;

import java.util.Objects;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月27日 09:51
 */
public class GloamObjectKey<T> {

    private final String key;
    private final Class<T> type;

    private GloamObjectKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
        this.checkSelf();
    }

    public static <NT> GloamObjectKey<NT> of(String key, Class<NT> type) {
        return new GloamObjectKey<>(key, type);
    }

    public static GloamObjectKey<Object> of(String key) {
        return of(key, Object.class);
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }

    private void checkSelf() {
        if (StringUtil.isBlank(key) || Objects.isNull(type)) {
            throw new GloamIllegalArgumentException("[GloamObjectKey]:GloamObjectKey必须指定keyName和对象类型");
        }
    }

    public boolean canTransform(Object obj) {
        return Objects.nonNull(obj) && this.type.isAssignableFrom(obj.getClass());
    }

    @SuppressWarnings("unchecked")
    public T transform(Object obj) {
        if (!canTransform(obj)) {
            throw new GloamNonSupportedFunctionException("[GloamObjectKey]:对象{}不能转为{}", obj, type);
        }
        return (T) obj;
    }
}
