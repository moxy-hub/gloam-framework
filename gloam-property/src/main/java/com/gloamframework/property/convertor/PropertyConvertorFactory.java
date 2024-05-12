package com.gloamframework.property.convertor;

import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import com.gloamframework.property.convertor.annotation.PropertyConvertorRegister;
import com.gloamframework.property.convertor.exception.PropertyConvertException;
import com.gloamframework.scanner.ResourceCentre;
import com.gloamframework.scanner.ResourceCentreFactory;
import org.apache.commons.logging.Log;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 属性转换工厂
 *
 * @author 晓龙
 */
public class PropertyConvertorFactory {

    private final Collection<PropertyConvertor> SUPPORT_CONVERTS;

    private static PropertyConvertorFactory propertyConvertorFactory;

    /**
     * spring 配置环境
     */
    private final ConfigurableEnvironment environment;
    private final Log log;

    public PropertyConvertorFactory(ConfigurableEnvironment environment, ClassLoader classLoader, Log log) {
        this.environment = environment;
        this.log = log;
        // 获取全部的转换器资源
        ResourceCentre resourceCentre;
        try {
            resourceCentre = ResourceCentreFactory.ofSingleDefault(classLoader, log);
        } catch (IOException e) {
            throw new PropertyConvertException("Get ResourceCentre Failed", e);
        }
        Set<Class<?>> convertClasses = resourceCentre.getResourcesClassesByAnnotation(PropertyConvertorRegister.GloamPropertyConvertorResource, PropertyConvertorRegister.class);
        Set<PropertyConvertor> propertyConvertorSet = new HashSet<>();
        for (Class<?> convertClass : convertClasses) {
            if (!PropertyConvertor.class.isAssignableFrom(convertClass)) {
                log.error("Create propertyConvertor failed,because the class:" + convertClass + " without implements" + PropertyConvertor.class + " interface");
                continue;
            }
            try {
                propertyConvertorSet.add((PropertyConvertor) convertClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new PropertyConvertException("Failed to new PropertyConvertor instance for class:{}", e, convertClass);
            }
        }
        // 排序
        SUPPORT_CONVERTS = propertyConvertorSet.stream().sorted(Comparator.comparingInt(Ordered::getOrder)).collect(Collectors.toList());
        propertyConvertorFactory = this;
    }

    public static PropertyConvertorFactory getSingle() {
        return propertyConvertorFactory;
    }

    public boolean canConvert(Class<?> propertyType) {
        return Objects.nonNull(getSupportPropertyValueConvertor(propertyType));
    }

    public Set<PropertyMapperDefinition> convert(String originPropertyPath, String mappingProperPath, Class<?> propertyType, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty) {
        PropertyConvertor supportPropertyValueConvertor = getSupportPropertyValueConvertor(propertyType);
        if (Objects.isNull(supportPropertyValueConvertor)) {
            throw new PropertyConvertException("Un support convert type:{}", propertyType);
        }
        return supportPropertyValueConvertor.convert(originPropertyPath, mappingProperPath, propertyType, environment, defaultFieldValue, mappingConfigurationProperty, log);
    }

    private PropertyConvertor getSupportPropertyValueConvertor(Class<?> propertyType) {
        // 遍历获取支持的转换器
        for (PropertyConvertor supportConvert : SUPPORT_CONVERTS) {
            if (supportConvert.canConvert(propertyType, environment)) {
                return supportConvert;
            }
        }
        return null;
    }
}
