package com.gloamframework.property;

import com.gloamframework.property.annotation.MappingConfigurationProperties;

/**
 * 配置映射接口,通过该接口，可以实现将@{@link MappingConfigurationProperties}注解的类中
 * 标注了@{@link com.gloamframework.property.annotation.MappingConfigurationProperty}注解的字段进行映射，将spring中的配置进行替换
 *
 * @author 晓龙
 */
public interface PropertyMapper {

    /**
     * 调用该方法，立刻对环境中的配置进行ø映射替换
     */
    void mapping();

}
