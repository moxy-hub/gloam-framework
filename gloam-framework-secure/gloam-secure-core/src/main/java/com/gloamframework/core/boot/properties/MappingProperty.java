package com.gloamframework.core.boot.properties;

import java.util.Set;

/**
 * 映射配置
 *
 * @author 晓龙
 */
public interface MappingProperty {

    /**
     * 收集指定包下的@MappingConfigurationProperty标识的字段为MappingPropertyDefinition
     */
    Set<MappingPropertyDefinition> collectMappingPropertyDefinitions(String... packagePath);

    /**
     * 将对应的配置映射为配置定义对象
     *
     * @param originalPath    原始路径
     * @param mappingPath     映射路径
     * @param mappingClass    映射类型
     * @param defaultProperty 默认值
     */
    Set<MappingPropertyDefinition> assembleMappingPropertyDefinition(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultProperty);

    default Set<MappingPropertyDefinition> assembleMappingPropertyDefinition(String originalPath, String mappingPath, Class<?> mappingClass) {
        return assembleMappingPropertyDefinition(originalPath, mappingPath, mappingClass, null);
    }

    /**
     * 将映射对象进行映射
     *
     * @param mappingPropertyDefinitions 映射对象集合
     */
    void doMapping(Set<MappingPropertyDefinition> mappingPropertyDefinitions);

}
