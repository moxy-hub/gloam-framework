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
import java.util.Objects;

/**
 * @author 晓龙
 */
public class DefaultPropertyPathAssembler implements PropertyPathAssembler {

    private static final Class<?>[] ignoreClasses = {String.class, DataSize.class};
    /**
     * 配置分割符
     */
    private static final char CONFIG_SPLIT = '.';

    @Override
    public void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, PathAnalysisAcquirer pathAnalysisAcquirer) {
        this.assemblePath(originalPath, mappingPath, mappingClass, null, pathAnalysisAcquirer);
    }

    public void assemblePath(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultValue, PathAnalysisAcquirer pathAnalysisAcquirer) {
        // 如果是基本数据类型或者String类型，则直接感知出去
        if (ClassUtils.isPrimitiveOrWrapper(mappingClass) || ArrayUtils.contains(ignoreClasses, mappingClass)) {
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
     * 重写字段的注入
     */
    private void assemblePath(String originalPath, String mappingPath, Field field, Object mappingObject, Object defaultValue, MappingConfigurationProperty mappingConfigurationProperty, PathAnalysisAcquirer pathAnalysisAcquirer) {
        // 处理路径
        String originPropertyPath = originalPath + this.convertFieldName2ConfigName(field.getName());
        // 目标路径
        String mappingProperPath = StrUtil.isBlank(mappingPath) ? mappingConfigurationProperty.mappingFor() : mappingPath + mappingConfigurationProperty.mappingFor();
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
