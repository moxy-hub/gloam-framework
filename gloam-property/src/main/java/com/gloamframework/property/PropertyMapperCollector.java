package com.gloamframework.property;

import java.util.Set;

/**
 * mapping收集器，会通过内部的资源中心中获取
 *
 * @author 晓龙
 */
public interface PropertyMapperCollector extends PropertyMapper {

    /**
     * 收集资源中心中的标注了@GloamConfigurationProperties注解的资源中使用@MappingConfigurationProperty标识的字段为MappingPropertyDefinition
     */
    Set<PropertyMapperDefinition> collectMappingPropertyDefinitions();

}
