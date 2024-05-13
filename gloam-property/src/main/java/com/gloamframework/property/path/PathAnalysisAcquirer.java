package com.gloamframework.property.path;

import com.gloamframework.property.annotation.MappingConfigurationProperty;

/**
 * 路径分析获取器，可以获取到具体的路径信息
 *
 * @author 晓龙
 */
@FunctionalInterface
public interface PathAnalysisAcquirer {

    /**
     * 在感知到路径后，会通过当前方法进行回调
     *
     * @param originPropertyPath           原始路径
     * @param mappingProperPath            映射路径
     * @param propertyType                 配置类型
     * @param defaultFieldValue            默认值
     * @param mappingConfigurationProperty 映射注解
     */
    void acquirer(String originPropertyPath,
                  String mappingProperPath,
                  Class<?> propertyType,
                  Object defaultFieldValue,
                  MappingConfigurationProperty mappingConfigurationProperty);
}
