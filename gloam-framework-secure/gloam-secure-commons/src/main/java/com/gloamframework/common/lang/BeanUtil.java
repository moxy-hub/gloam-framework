package com.gloamframework.common.lang;

import com.gloamframework.common.collection.CollectionUtils;

import java.util.List;

public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    public static <T> T toBean(Object source, Class<T> targetClass) {
        return cn.hutool.core.bean.BeanUtil.toBean(source, targetClass);
    }

    public static <S, T> List<T> toBean(List<S> source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        return CollectionUtils.convertList(source, s -> toBean(s, targetType));
    }

}
