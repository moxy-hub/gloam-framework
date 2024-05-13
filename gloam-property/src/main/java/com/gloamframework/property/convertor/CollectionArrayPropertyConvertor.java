package com.gloamframework.property.convertor;

import cn.hutool.core.util.ArrayUtil;
import com.gloamframework.property.convertor.annotation.PropertyConvertorRegister;
import com.gloamframework.property.utils.TernaryHashMap;
import org.apache.commons.logging.Log;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Collection;
import java.util.Objects;

/**
 * 集合｜数组的转换器，实现于复杂转换器
 *
 * @author 晓龙
 */
@PropertyConvertorRegister
public class CollectionArrayPropertyConvertor extends AbstractComplexPropertyConvertor {
    @Override
    protected TernaryHashMap<String, String, Object> complexConvert(ConfigurableEnvironment environment, String originPropertyPath, String mappingProperPath, Object defaultFieldValue, Class<?> nestedPropertyType, TernaryHashMap<String, String, Object> pathDefaultPropertyMappings, Log log) {
        if (Objects.isNull(defaultFieldValue)) {
            return pathDefaultPropertyMappings;
        }
        // 处理集合数组
        Object[] arrays = null;
        if (Collection.class.isAssignableFrom(defaultFieldValue.getClass())) {
            @SuppressWarnings("unchecked")
            Collection<Object> collectionValue = (Collection<Object>) defaultFieldValue;
            arrays = ArrayUtil.toArray(collectionValue, Object.class);
        } else if (defaultFieldValue.getClass().isArray()) {
            arrays = (Object[]) defaultFieldValue;
        }
        if (ArrayUtil.isNotEmpty(arrays)) {
            for (int index = 0; index < arrays.length; index++) {
                // 拼接原始路径
                String origin = originPropertyPath + "[" + index + "]";
                // 拼接目标路径
                String mapping = mappingProperPath + "[" + index + "]";
                if (String.class.isAssignableFrom(nestedPropertyType)) {
                    pathDefaultPropertyMappings.put(origin, mapping, arrays[index]);
                } else {
                    pathDefaultPropertyMappings.put(origin, mapping, null);
                }
            }
        }
        return pathDefaultPropertyMappings;
    }

    @Override
    public boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment) {
        return Collection.class.isAssignableFrom(propertyType) || propertyType.isArray();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
