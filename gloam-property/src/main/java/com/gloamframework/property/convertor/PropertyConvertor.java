package com.gloamframework.property.convertor;

import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import org.apache.commons.logging.Log;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * 配置映射器，由业务进行实现，对不同的配置对象进行映射
 *
 * @author 晓龙
 */
public interface PropertyConvertor extends Ordered {
    Set<PropertyMapperDefinition> convert(String originPropertyPath, String mappingProperPath, Class<?> propertyType, ConfigurableEnvironment environment, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty, Log log);

    boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment);

}
