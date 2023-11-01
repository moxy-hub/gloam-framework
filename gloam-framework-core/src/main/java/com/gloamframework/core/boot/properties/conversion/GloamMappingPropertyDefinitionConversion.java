package com.gloamframework.core.boot.properties.conversion;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.map.TernaryHashMap;
import com.gloamframework.core.boot.properties.MappingPropertyDefinition;
import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import com.gloamframework.core.boot.properties.exception.MappingPropertyException;
import com.gloamframework.core.boot.properties.exception.MappingPropertyInstanceException;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p><b>gloam配置转换器</b></p>
 * <li>识别传入class类型，对于spring支持转换的基本类型将直接进行转换
 * <li>如果spring不支持转换，则会对传入的class进行解析，如果存在标识了{@link MappingConfigurationProperty}注解的字段进行映射匹配
 * <li>如果字段存在{@link NestedConfigurationProperty}嵌套配置，将会把该字段类型作为传入class进行递归映射
 *
 * @author 晓龙
 */
public class GloamMappingPropertyDefinitionConversion implements MappingPropertyDefinitionConversion {

    /**
     * 配置分割符
     */
    private static final char CONFIG_SPLIT = '.';
    /**
     * spring 配置环境
     */
    private final ConfigurableEnvironment environment;
    /**
     * spring配置转换服务
     */
    private final ConfigurableConversionService conversionService;

    public GloamMappingPropertyDefinitionConversion(ConfigurableEnvironment environment) {
        this.environment = environment;
        this.conversionService = this.environment.getConversionService();
    }

    @Override
    public Set<MappingPropertyDefinition> convert(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultProperty) {
        Set<MappingPropertyDefinition> definitions = new HashSet<>();
        // 检查传入的配置类型spring是否可以直接解析
        if (conversionService.canConvert(String.class, mappingClass)) {
            String defaultValue = null;
            // 转换默认值
            if (defaultProperty != null && conversionService.canConvert(String.class, defaultProperty.getClass())) {
                defaultValue = conversionService.convert(defaultProperty, String.class);
            }
            if (StrUtil.isBlank(defaultValue)) {
                defaultValue = "";
            }
            // 在spring 环境中拿值
            String property = environment.getProperty(originalPath, defaultValue);
            if (StrUtil.isBlank(property)) {
                return definitions;
            }
            definitions.add(new MappingPropertyDefinition(originalPath, mappingPath, property));
            return definitions;
        }
        // 处理配置路径
        if (StrUtil.isNotBlank(originalPath)) {
            originalPath = StrUtil.endWith(originalPath, CONFIG_SPLIT) ? originalPath : originalPath + CONFIG_SPLIT;
        }
        if (StrUtil.isNotBlank(mappingPath)) {
            mappingPath = StrUtil.endWith(mappingPath, CONFIG_SPLIT) ? mappingPath : mappingPath + CONFIG_SPLIT;
        }
        // 实例化类
        Object mappingObject;
        try {
            mappingObject = mappingClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MappingPropertyInstanceException("实例重写配置对象：{} 失败", e, mappingClass);
        }

        // 处理字段上标注了OverrideProperty的注解
        Field[] declaredFields = mappingClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 获取注解
            MappingConfigurationProperty mappingConfigurationProperty = AnnotationUtils.findAnnotation(field, MappingConfigurationProperty.class);
            if (mappingConfigurationProperty == null) {
                continue;
            }
            // 处理具体字段
            this.mappingFieldInjectSpringSupport(originalPath, mappingPath, field, mappingObject, mappingConfigurationProperty, definitions);
        }
        return definitions;
    }

    /**
     * 重写字段的注入
     */
    private void mappingFieldInjectSpringSupport(String originalPath, String mappingPath, Field field, Object mappingObject, MappingConfigurationProperty mappingConfigurationProperty, Set<MappingPropertyDefinition> definitions) {
        // 处理路径
        String originPropertyPath = originalPath + this.convertFieldName2ConfigName(field.getName());
        // 目标路径
        String mappingProperPath = StrUtil.isBlank(mappingPath) ? mappingConfigurationProperty.mappingFor() : mappingPath + mappingConfigurationProperty.mappingFor();
        Class<?> propertyType = field.getType();
        // 嵌套类型递归解析
        if (AnnotationUtils.findAnnotation(field, NestedConfigurationProperty.class) != null) {
            definitions.addAll(this.convert(originPropertyPath, mappingProperPath, propertyType, null));
            return;
        }
        // 判断字段类型，对map，collection，array处理
        if (Map.class.isAssignableFrom(propertyType) || Collection.class.isAssignableFrom(propertyType) || propertyType.isArray()) {
            this.mappingFieldGloamSupport(originPropertyPath, mappingProperPath, field, mappingObject, mappingConfigurationProperty, definitions);
            return;
        }
        // 默认值
        String defaultValue = null;
        Object fieldValue = ReflectUtil.getFieldValue(mappingObject, field);
        if (fieldValue != null && conversionService.canConvert(String.class, fieldValue.getClass())) {
            defaultValue = conversionService.convert(fieldValue, String.class);
        }
        if (StrUtil.isBlank(defaultValue)) {
            defaultValue = "";
        }
        // 获取配置值
        String property = environment.getProperty(originPropertyPath, defaultValue);
        definitions.add(new MappingPropertyDefinition(originPropertyPath, mappingProperPath, property));
    }

    /**
     * 处理非spring默认支持的字段类型
     * 目前只处理三种
     * MAP类型、Collection类型、Array类型
     */
    private void mappingFieldGloamSupport(String originalPath, String mappingPath, Field field, Object mappingObject, MappingConfigurationProperty mappingConfigurationProperty, Set<MappingPropertyDefinition> definitions) {
        /*
         * 主要解决的问题是将特殊类型的路径进行拼接
         * 1、循环本地的配置，将路径和默认值存储
         * 2、循环spring环境配置，将路径存储
         */
        // 判断类型
        Class<?> nestedPropertyType = mappingConfigurationProperty.nestedPropertyType();
        if (nestedPropertyType == null) {
            nestedPropertyType = MappingConfigurationProperty.NoNestedConfigurationPropertyType.class;
        }
        if (MappingConfigurationProperty.NoNestedConfigurationPropertyType.class.isAssignableFrom(nestedPropertyType)) {
            nestedPropertyType = String.class;
        }
        // 如果没有指定嵌套对象类型，将默认使用spring的转换器，转成String
        Class<?> finalNestedPropertyType = nestedPropertyType;
        // 使用三元map进行映射
        TernaryHashMap<String, String, Object> pathDefaultPropertyMappings = new TernaryHashMap<>();
        // 获取spring环境中的配置路径
        Iterator<PropertySource<?>> sourceIterator = environment.getPropertySources().stream().iterator();
        while (sourceIterator.hasNext()) {
            PropertySource<?> propertySource = sourceIterator.next();
            // 处理可以获取名字的配置
            if (!EnumerablePropertySource.class.isAssignableFrom(propertySource.getClass())) {
                continue;
            }
            String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
            // 找到符合名字的配置
            for (String propertyName : propertyNames) {
                if (!StrUtil.startWith(propertyName, originalPath)) {
                    continue;
                }
                // 处理目标路径
                String mapConfig = propertyName.replaceAll(originalPath, "");
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
                String origin = originalPath + mapConfig;
                String mapping = mappingPath + mapConfig;
                pathDefaultPropertyMappings.put(origin, mapping, null);
            }
        }
        // 获取对象默认值
        Object fieldValue = ReflectUtil.getFieldValue(mappingObject, field);
        if (fieldValue != null) {
            // 处理map
            if (Map.class.isAssignableFrom(field.getType())) {
                Map<?, ?> mapValue = (Map<?, ?>) fieldValue;
                mapValue.forEach((k, v) -> {
                    if (!conversionService.canConvert(String.class, k.getClass())) {
                        throw new MappingPropertyException("环境配置使用MAP字段:{}时，无法正确解析MAP的key类型:{}", field.getName(), k.getClass());
                    }
                    String currentPropertyPath = conversionService.convert(k, String.class);
                    // 拼接原始路径
                    String origin = originalPath + CONFIG_SPLIT + currentPropertyPath;
                    // 拼接目标路径
                    String mapping = mappingPath + CONFIG_SPLIT + currentPropertyPath;
                    if (String.class.isAssignableFrom(finalNestedPropertyType)) {
                        if (!conversionService.canConvert(String.class, v.getClass())) {
                            throw new MappingPropertyException("配置环境不支持的对象解析:{}", v.getClass());
                        }
                        pathDefaultPropertyMappings.put(origin, mapping, conversionService.convert(v, String.class));
                    } else {
                        pathDefaultPropertyMappings.put(origin, mapping, null);
                    }
                });
            } else {
                // 处理集合数组
                Object[] arrays = null;
                if (Collection.class.isAssignableFrom(field.getType())) {
                    @SuppressWarnings("unchecked")
                    Collection<Object> collectionValue = (Collection<Object>) fieldValue;
                    arrays = ArrayUtil.toArray(collectionValue, Object.class);
                } else if (field.getType().isArray()) {
                    arrays = (Object[]) fieldValue;
                }
                if (ArrayUtil.isNotEmpty(arrays)) {
                    for (int index = 0; index < arrays.length; index++) {
                        // 拼接原始路径
                        String origin = originalPath + "[" + index + "]";
                        // 拼接目标路径
                        String mapping = mappingPath + "[" + index + "]";
                        if (String.class.isAssignableFrom(finalNestedPropertyType)) {
                            if (!conversionService.canConvert(String.class, arrays[index].getClass())) {
                                throw new MappingPropertyException("配置环境不支持的对象解析:{}", arrays[index].getClass());
                            }
                            pathDefaultPropertyMappings.put(origin, mapping, conversionService.convert(arrays[index], String.class));
                        } else {
                            pathDefaultPropertyMappings.put(origin, mapping, null);
                        }
                    }
                }
            }
        }
        pathDefaultPropertyMappings.forEach((origin, value) -> {
            // 目标值
            String mapping = value.getValue1();
            // 默认值
            Object defaultProperty = value.getValue2();
            // 递归进行拿值
            definitions.addAll(this.convert(origin, mapping, finalNestedPropertyType, defaultProperty));
        });
    }


    /**
     * 将驼峰转换为"-"模式
     */
    private String convertFieldName2ConfigName(String fieldName) {
        fieldName = fieldName.replaceAll("([A-Z])", "-$1").toLowerCase();
        return StrUtil.startWith(fieldName, "-") ? StrUtil.subSuf(fieldName, 1) : fieldName;
    }

}
