package com.gloamframework.property;

import java.util.Set;

/**
 * 映射定义对象收集器，通过{@link com.gloamframework.scanner.ResourceCentre}资源中心进行内部的资源扫描，
 * 获取到标注了对应注解的类，并解析配置字段，将所有的映射配置封装成{@link PropertyMapperDefinition}定义对象
 *
 * @author 晓龙
 */
public interface PropertyMapperCollector extends PropertyMapper {

    /**
     * 收集资源中心中的标注了@GloamConfigurationProperties注解的资源中使用@MappingConfigurationProperty标识的字段为MappingPropertyDefinition
     */
    PropertyMapperDefinitionSet collectMappingPropertyDefinitions();

}
