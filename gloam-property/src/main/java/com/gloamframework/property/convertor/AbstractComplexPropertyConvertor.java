package com.gloamframework.property.convertor;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.PropertyMapperDefinition;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import com.gloamframework.property.utils.TernaryHashMap;
import org.apache.commons.logging.Log;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 复杂对象的抽象处理层，拼接复杂对象的路径，比如map，collection等特殊的路径，获取spring配置中的配置，并进行映射
 *
 * @author 晓龙
 */
public abstract class AbstractComplexPropertyConvertor implements PropertyConvertor {

    /**
     * 配置分割符
     */
    protected static final char CONFIG_SPLIT = '.';

    @Override
    public Set<PropertyMapperDefinition> convert(String originPropertyPath, String mappingProperPath, Class<?> propertyType, ConfigurableEnvironment environment, Object defaultFieldValue, MappingConfigurationProperty mappingConfigurationProperty, Log log) {
        if (Objects.isNull(mappingConfigurationProperty)) {
            log.error("Can not mapping property for type:" + propertyType);
            return Collections.singleton(new PropertyMapperDefinition().setOriginalPath(originPropertyPath)
                    .setMappingPath(mappingProperPath));
        }
        /*
         * 主要解决的问题是将特殊类型的路径进行拼接
         * 1、循环本地的配置，将路径和默认值存储
         * 2、循环spring环境配置，将路径存储
         */
        // 判断类型
        Class<?> nestedPropertyType = mappingConfigurationProperty.nestedPropertyType();
        if (Objects.isNull(nestedPropertyType)) {
            nestedPropertyType = MappingConfigurationProperty.NoNestedConfigurationPropertyType.class;
        }
        // 如果没有指定嵌套对象类型，将默认使用spring的转换器，转成String
        if (MappingConfigurationProperty.NoNestedConfigurationPropertyType.class.isAssignableFrom(nestedPropertyType)) {
            nestedPropertyType = String.class;
        }
        // 获取spring环境中的配置路径
        Iterator<PropertySource<?>> sourceIterator = environment.getPropertySources().stream().iterator();
        // 使用三元map进行映射
        TernaryHashMap<String, String, Object> pathDefaultPropertyMappings = new TernaryHashMap<>();
        while (sourceIterator.hasNext()) {
            PropertySource<?> propertySource = sourceIterator.next();
            // 处理可以获取名字的配置
            if (!EnumerablePropertySource.class.isAssignableFrom(propertySource.getClass())) {
                continue;
            }
            String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
            // 找到符合名字的配置
            for (String propertyName : propertyNames) {
                if (!StrUtil.startWith(propertyName, originPropertyPath)) {
                    continue;
                }
                // 处理目标路径
                String mapConfig = propertyName.replaceAll(originPropertyPath, "");
                if (StrUtil.isBlank(mapConfig)) {
                    continue;
                }
                // 如果map的value是嵌套对象，则截取后面值
                if (!String.class.isAssignableFrom(nestedPropertyType)) {
                    int index;
                    if ((index = mapConfig.lastIndexOf(CONFIG_SPLIT)) != -1) {
                        mapConfig = mapConfig.substring(0, index);
                    }
                }
                String origin = originPropertyPath + mapConfig;
                String mapping = mappingProperPath + mapConfig;
                pathDefaultPropertyMappings.put(origin, mapping, null);
            }
        }
        TernaryHashMap<String, String, Object> ternaryHashMap = this.complexConvert(environment, originPropertyPath, mappingProperPath, defaultFieldValue, nestedPropertyType, pathDefaultPropertyMappings, log);
        return this.checkNestedObject(ternaryHashMap, nestedPropertyType);
    }

    private Set<PropertyMapperDefinition> checkNestedObject(TernaryHashMap<String, String, Object> pathDefaultPropertyMappings, Class<?> nestedPropertyType) {
        Set<PropertyMapperDefinition> definitions = new HashSet<>();
        PropertyConvertorFactory propertyConvertorFactory = PropertyConvertorFactory.getSingle();
        pathDefaultPropertyMappings.forEach((origin, value) -> {
            // 目标值
            String mapping = value.getValue1();
            // 默认值
            Object defaultProperty = value.getValue2();
            if (propertyConvertorFactory.canConvert(nestedPropertyType)) {
                // 递归进行拿值
                definitions.addAll(propertyConvertorFactory.convert(origin, mapping, nestedPropertyType, defaultProperty, null));
            } else {
                Field[] declaredFields = nestedPropertyType.getDeclaredFields();
                for (Field field : declaredFields) {
                    // 获取注解
                    MappingConfigurationProperty mappingConfigurationProperty = AnnotationUtils.findAnnotation(field, MappingConfigurationProperty.class);
                    if (mappingConfigurationProperty == null) {
                        continue;
                    }
                    // 处理具体字段
                    definitions.addAll(propertyConvertorFactory.convert(origin, mapping, nestedPropertyType, defaultProperty, mappingConfigurationProperty));
                }
            }
        });
        return definitions;
    }

    protected abstract TernaryHashMap<String, String, Object> complexConvert(
            ConfigurableEnvironment environment,
            String originPropertyPath,
            String mappingProperPath,
            Object defaultFieldValue,
            Class<?> nestedPropertyType,
            TernaryHashMap<String, String, Object> pathDefaultPropertyMappings,
            Log log);
}
