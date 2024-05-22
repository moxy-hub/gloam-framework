package com.gloamframework.property.path;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import com.gloamframework.property.exception.MappingPropertyInstanceException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.unit.DataSize;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 默认实现的基础路径装配器
 *
 * @author 晓龙
 */
public class DefaultPropertyPathAssembler implements PropertyPathAssembler {

    /**
     * 忽略的class字段，在基本数据类型基础上增加，防止标注了{@link NestedConfigurationProperty}注解的基础类型，即使解析进去也是没有结果
     */
    private static final Class<?>[] ignoreClasses = {String.class, DataSize.class, Map.class, Collection.class};
    /**
     * 配置分割符
     */
    private static final char CONFIG_SPLIT = '.';

    @Override
    public void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, PathAnalysisAcquirer pathAnalysisAcquirer) {
        this.assemblePath(originalPath, mappingPath, mappingClass, null, pathAnalysisAcquirer);
    }

    /**
     * 这里重载方法，方便内部进行递归解析
     */
    public void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultValue, PathAnalysisAcquirer pathAnalysisAcquirer) {
        // 如果是基本数据类型或者String类型，则直接感知出去
        if (ClassUtils.isPrimitiveOrWrapper(mappingClass) || ArrayUtils.contains(ignoreClasses, mappingClass) || mappingClass.isArray()) {
            pathAnalysisAcquirer.acquirer(originalPath, mappingPath, mappingClass, defaultValue, null);
            return;
        }
        // 处理路径，统一在后面添加配置的分隔符
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
        // 处理字段上标注了MappingConfigurationProperty的注解
        Field[] declaredFields = mappingClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 获取注解
            MappingConfigurationProperty mappingConfigurationProperty = AnnotationUtils.findAnnotation(field, MappingConfigurationProperty.class);
            if (mappingConfigurationProperty == null) {
                continue;
            }
            // 处理具体字段
            this.assemblePath(originalPath, mappingPath, field, mappingObject, defaultValue, mappingConfigurationProperty, pathAnalysisAcquirer);
        }
    }

    /**
     * 具体的字段解析与凭借
     */
    private void assemblePath(String originalPath, String mappingPath, Field field, Object mappingObject, Object defaultValue, MappingConfigurationProperty mappingConfigurationProperty, PathAnalysisAcquirer pathAnalysisAcquirer) {
        // 处理注解的映射路径，如果为空，则映射属性名
        String mappingFor = mappingConfigurationProperty.mappingFor();
        if (StrUtil.isBlank(mappingFor)) {
            mappingFor = this.convertFieldName2ConfigName(field.getName());
        }
        // 处理路径
        String originPropertyPath = originalPath + this.convertFieldName2ConfigName(field.getName());
        // 目标路径
        String mappingProperPath = StrUtil.isBlank(mappingPath) ? mappingFor : mappingPath + mappingFor;
        Class<?> propertyType = field.getType();
        // 嵌套类型递归解析
        if (AnnotationUtils.findAnnotation(field, NestedConfigurationProperty.class) != null) {
            this.assemblePath(originPropertyPath, mappingProperPath, propertyType, defaultValue, pathAnalysisAcquirer);
            return;
        }
        // 将路径进行通知
        pathAnalysisAcquirer.acquirer(originPropertyPath, mappingProperPath, propertyType, Objects.isNull(defaultValue) ? ReflectUtil.getFieldValue(mappingObject, field) : defaultValue, mappingConfigurationProperty);
    }

    /**
     * 将驼峰转换为"-"模式
     */
    private String convertFieldName2ConfigName(String fieldName) {
        fieldName = fieldName.replaceAll("([A-Z])", "-$1").toLowerCase();
        return StrUtil.startWith(fieldName, "-") ? StrUtil.subSuf(fieldName, 1) : fieldName;
    }
}
