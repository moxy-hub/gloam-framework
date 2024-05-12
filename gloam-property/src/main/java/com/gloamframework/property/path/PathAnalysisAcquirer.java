package com.gloamframework.property.path;

import com.gloamframework.property.annotation.MappingConfigurationProperty;

/**
 * 路径分析获取器，可以获取到具体的路径信息
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface PathAnalysisAcquirer {

    void acquirer(String originPropertyPath, String mappingProperPath, Class<?> propertyType, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty);
}
