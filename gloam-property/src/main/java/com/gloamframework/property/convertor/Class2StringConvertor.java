package com.gloamframework.property.convertor;

import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import com.gloamframework.property.convertor.annotation.PropertyConvertorRegister;
import org.apache.commons.logging.Log;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 支持class对象的地址转换
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年10月16日 19:44
 */
@PropertyConvertorRegister
public class Class2StringConvertor implements PropertyConvertor {
    @Override
    public Set<PropertyMapperDefinition> convert(String originPropertyPath, String mappingProperPath, Class<?> propertyType, ConfigurableEnvironment environment, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty, Log log) {
        String property = Objects.isNull(defaultFieldValue) ? "" : ((Class<?>) defaultFieldValue).getCanonicalName();
        property = environment.getProperty(originPropertyPath, property);
        return Collections.singleton(new PropertyMapperDefinition().setOriginalPath(originPropertyPath)
                .setMappingPath(mappingProperPath).setValue(property));
    }

    @Override
    public boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment) {
        return Class.class.isAssignableFrom(propertyType);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
