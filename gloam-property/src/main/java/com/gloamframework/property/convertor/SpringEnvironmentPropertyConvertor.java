package com.gloamframework.property.convertor;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import com.gloamframework.property.convertor.annotation.PropertyConvertorRegister;
import org.apache.commons.logging.Log;
import org.springframework.core.Ordered;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * spring环境中默认的转换器支持
 *
 * @author 晓龙
 */
@PropertyConvertorRegister
public class SpringEnvironmentPropertyConvertor implements PropertyConvertor {
    @Override
    public Set<PropertyMapperDefinition> convert(String originPropertyPath, String mappingProperPath, Class<?> propertyType, ConfigurableEnvironment environment, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty, Log log) {
        if (!this.canConvert(propertyType, environment)) {
            log.error("Can not mapping property for type:" + propertyType);
            return Collections.singleton(new PropertyMapperDefinition().setOriginalPath(originPropertyPath)
                    .setMappingPath(mappingProperPath));
        }
        ConfigurableConversionService conversionService = environment.getConversionService();
        // 默认值
        String defaultValue = null;
        if (defaultFieldValue != null && conversionService.canConvert(String.class, defaultFieldValue.getClass())) {
            defaultValue = conversionService.convert(defaultFieldValue, String.class);
        }
        if (StrUtil.isBlank(defaultValue)) {
            defaultValue = null;
        }
        // 获取配置值
        String property = environment.getProperty(originPropertyPath, defaultValue);
        return Collections.singleton(new PropertyMapperDefinition().setOriginalPath(originPropertyPath)
                .setMappingPath(mappingProperPath).setValue(property));
    }

    @Override
    public boolean canConvert(Class<?> propertyType, ConfigurableEnvironment environment) {
        if (Objects.isNull(environment)) {
            return false;
        }
        return environment.getConversionService().canConvert(String.class, propertyType);
    }

    @Override
    public int getOrder() {
        // 最后触发spring的转换
        return Ordered.LOWEST_PRECEDENCE;
    }
}
