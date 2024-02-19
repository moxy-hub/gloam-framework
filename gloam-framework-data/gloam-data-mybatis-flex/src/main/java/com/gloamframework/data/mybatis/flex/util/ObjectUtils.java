package com.gloamframework.data.mybatis.flex.util;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 晓龙
 */
@Slf4j
public class ObjectUtils {

    /**
     * 填充对象对应的字段，如果字段存在值，则跳过填充，对象字段必须符合标准的Java Bean
     *
     * @param source 填充对象
     * @param field  填充字段名
     * @param value  填充值
     */
    public static void fillField(Object source, String field, Object value) {
        // 校验是否存在字段
        if (!ReflectUtil.hasField(source.getClass(), field)) {
            log.warn("[对象填充]:对应字段不存在,跳过填充");
            return;
        }
        Object defaultValue = ReflectUtil.getFieldValue(source, field);
        if (Objects.nonNull(defaultValue)) {
            log.debug("[对象填充]:对应字段存在默认值,跳过填充");
            return;
        }
        ReflectUtil.setFieldValue(source, field, value);
    }

}
