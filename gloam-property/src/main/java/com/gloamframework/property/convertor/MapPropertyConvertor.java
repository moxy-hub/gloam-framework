package com.gloamframework.property.convertor;

import com.gloamframework.property.convertor.annotation.PropertyConvertorRegister;
import com.gloamframework.property.exception.MappingPropertyException;
import com.gloamframework.property.utils.TernaryHashMap;
import org.apache.commons.logging.Log;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;
import java.util.Objects;

/**
 * @author 晓龙
 */
@PropertyConvertorRegister
public class MapPropertyConvertor extends AbstractComplexPropertyConvertor {

    @Override
    protected TernaryHashMap<String, String, Object> complexConvert(ConfigurableEnvironment environment, String originPropertyPath, String mappingProperPath, Object defaultFieldValue, Class<?> nestedPropertyType, TernaryHashMap<String, String, Object> pathDefaultPropertyMappings, Log log) {
        if (Objects.isNull(defaultFieldValue)) {
            return pathDefaultPropertyMappings;
        }
        // 处理map
        Map<?, ?> mapValue = (Map<?, ?>) defaultFieldValue;
        mapValue.forEach((k, v) -> {
            if (!String.class.isAssignableFrom(k.getClass())) {
                throw new MappingPropertyException("环境配置使用MAP字段时，无法正确解析MAP的key类型:{}", k.getClass());
            }
            String currentPropertyPath = (String) k;
            // 拼接原始路径
            String origin = originPropertyPath + CONFIG_SPLIT + currentPropertyPath;
            // 拼接目标路径
            String mapping = mappingProperPath + CONFIG_SPLIT + currentPropertyPath;
            if (String.class.isAssignableFrom(nestedPropertyType)) {
                pathDefaultPropertyMappings.put(origin, mapping, v);
            } else {
                pathDefaultPropertyMappings.put(origin, mapping, null);
            }
        });
        return pathDefaultPropertyMappings;
    }

    @Override
    public boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment) {
        return Map.class.isAssignableFrom(propertyType);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
