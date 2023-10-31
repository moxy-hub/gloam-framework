package com.gloamframework.core.boot.properties.conversion;

import com.gloamframework.core.boot.properties.MappingPropertyDefinition;

import java.util.Set;

/**
 * 配置映射定义转换器
 *
 * @author 晓龙
 */
public interface MappingPropertyDefinitionConversion {

    /**
     * 将条件符合的对象进行解析，转为映射定义对象
     *
     * @param originalPath    原始路径
     * @param mappingPath     映射路径
     * @param mappingClass    映射类
     * @param defaultProperty 默认值
     * @return 映射定义对象集合
     */
    Set<MappingPropertyDefinition> convert(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultProperty);

}
