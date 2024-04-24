package com.gloamframework.property;

import java.util.Set;

/**
 * mapping收集器，会通过内部的资源中心中获取
 *
 * @author 晓龙
 */
public interface MappingPropertyCollector extends MappingProperty {

    /**
     * 收集资源中心中的标注了@GloamConfigurationProperties注解的资源中使用@MappingConfigurationProperty标识的字段为MappingPropertyDefinition
     */
    Set<MappingPropertyDefinition> collectMappingPropertyDefinitions();

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

}
