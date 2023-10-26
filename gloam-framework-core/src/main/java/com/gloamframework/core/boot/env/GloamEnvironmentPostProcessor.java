package com.gloamframework.core.boot.env;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.env.exception.EnvironmentConfigException;
import com.gloamframework.core.boot.env.exception.EnvironmentInstanceException;
import com.gloamframework.core.boot.exception.GloamRuntimeException;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Gloam环境前置处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * gloam banner
     */
    private static final Banner GLOAM_BANNER = new GloamBanner();

    /**
     * gloam scanner
     */
    private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();

    /**
     * gloam扫描基础包
     */
    private static final String ASSEMBLE_BASE_PACKAGE = "com.gloam";

    /**
     * gloam环境配置名称
     */
    private static final String GLOAM_CONFIG_NAMESPACE = "gloamEnvironment";

    /**
     * 配置分割符
     */
    private static final char CONFIG_SPLIT = '.';

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        // 进行包扫描，扫描需要被重写的配置资源
        List<Class<?>> overrideClasses = this.scannerClassWithOverrideClass(application.getClassLoader(), ASSEMBLE_BASE_PACKAGE, application.getMainApplicationClass().getPackage().getName());
        // 获取映射
        List<OverrideMapping> overrideMappings = this.acquireOverrideMappings(environment, overrideClasses);
        // 获取环境中的配置
        Map<String, Object> propertySource = this.getOverrideMapPropertySource(environment).getSource();
        // 对资源进行重写映射
        if (CollectionUtil.isEmpty(overrideMappings)) {
            return;
        }
        for (OverrideMapping overrideMapping : overrideMappings) {
            propertySource.put(overrideMapping.getTargetProperty(), overrideMapping.getValue());
            System.out.println(StrUtil.format("config property override: {} -> {} = {}", overrideMapping.getOriginalProperty(), overrideMapping.getTargetProperty(), overrideMapping.getValue()));
        }
    }

    /**
     * 扫描获取标注了@OverrideClass注解的类
     */
    private List<Class<?>> scannerClassWithOverrideClass(ClassLoader classLoader, String... targetPackages) {
        List<Class<?>> classes = new ArrayList<>();
        for (String targetPackage : targetPackages) {
            // 扫描符合要求的包，OverrideClass标注的类
            try {
                classes.addAll(resourceScanner.scannerForClassesWithAnnotation(targetPackage, classLoader, OverrideClass.class));
            } catch (IOException e) {
                throw new GloamRuntimeException("获取资源失败", "配置重写映射失败", e);
            }
        }
        return classes;
    }

    /**
     * 获取spring环境中的gloam环境参数，如果没有的则进行创建
     *
     * @param environment spring环境
     */
    private MapPropertySource getOverrideMapPropertySource(ConfigurableEnvironment environment) {
        MapPropertySource propertySource = (MapPropertySource) environment.getPropertySources().get(GLOAM_CONFIG_NAMESPACE);
        if (propertySource != null) {
            return propertySource;
        }
        propertySource = new MapPropertySource(GLOAM_CONFIG_NAMESPACE, new HashMap<>());
        environment.getPropertySources().addLast(propertySource);
        return propertySource;
    }

    /**
     * 将对应的类进行映射，映射为可进行重写的对象
     *
     * @param overrideClasses 需要重写的类
     */
    private List<OverrideMapping> acquireOverrideMappings(ConfigurableEnvironment environment, List<Class<?>> overrideClasses) {
        List<OverrideMapping> mappings = new ArrayList<>();
        // 遍历重写类，进行映射
        for (Class<?> overrideClass : overrideClasses) {
            // 获取spring的配置前缀
            String springConfigPrefix = getSpringPropertyPrefix(overrideClass);
            mappings.addAll(this.acquireOverrideMappings(environment, springConfigPrefix, null, overrideClass));
        }
        return mappings;
    }

    /**
     * 第一版内容，经测试功能不完善，已废弃
     */
    @Deprecated
    private List<OverrideMapping> acquireOverrideMappingsDeprecated(ConfigurableEnvironment environment, String springConfigPrefix, Class<?> overrideClass) {
        List<OverrideMapping> mappings = new ArrayList<>();
        // 检查当前类是否存在标记了@OverrideProperty注解的字段
        Field[] declaredFields = overrideClass.getDeclaredFields();
        Object overrideObject = null;
        for (Field field : declaredFields) {
            // 处理OverrideProperty
            // 获取注解
            OverrideProperty overrideProperty = AnnotationUtils.findAnnotation(field, OverrideProperty.class);
            if (overrideProperty == null) {
                continue;
            }
            springConfigPrefix = StrUtil.endWith(springConfigPrefix, CONFIG_SPLIT) ? springConfigPrefix : springConfigPrefix + CONFIG_SPLIT;
            // 获取注解的源配置路径
            String originalConfigPath = springConfigPrefix + this.convertFieldName2ConfigName(field.getName());
            // 替换目标配置路径
            String targetConfigPath = overrideProperty.target();
            // 替换值,先拿spring properties中的
            Iterator<PropertySource<?>> sourceIterator = environment.getPropertySources().stream().iterator();
            while (sourceIterator.hasNext()) {
                PropertySource<?> propertySource = sourceIterator.next();
                if (!EnumerablePropertySource.class.isAssignableFrom(propertySource.getClass())) {
                    continue;
                }
                @SuppressWarnings("all")
                String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
                // 找到符合名字的配置
                for (String propertyName : propertyNames) {
                    if (!StrUtil.startWith(propertyName, originalConfigPath)) {
                        continue;
                    }
                    final String original = originalConfigPath;
                    originalConfigPath = propertyName;
                    // 处理目标路径
                    String mapConfig = originalConfigPath.replaceAll(original, "");
                    if (StrUtil.isBlank(mapConfig)) {
                        continue;
                    }
                    if (StrUtil.endWith(targetConfigPath, CONFIG_SPLIT)) {
                        targetConfigPath = targetConfigPath.substring(0, targetConfigPath.lastIndexOf(CONFIG_SPLIT));
                    }
                    targetConfigPath = targetConfigPath + mapConfig;
                }
            }
            String property = environment.getProperty(originalConfigPath);
            if (StrUtil.isNotBlank(property)) {
                mappings.add(new OverrideMapping(originalConfigPath, targetConfigPath, property));
                continue;
            }
            // 再拿对象中的
            if (overrideObject == null) {
                try {
                    overrideObject = overrideClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new EnvironmentInstanceException("实例重写配置对象：{} 失败", e, overrideClass);
                }
            }
            // 通过spring的转换器，将配置进行转换
            ConfigurableConversionService conversionService = environment.getConversionService();
            // 对于map需要特殊处理
            if (Map.class.isAssignableFrom(field.getType())) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> fieldMapValue = (Map<Object, Object>) ReflectUtil.getFieldValue(overrideObject, field);
                for (Map.Entry<Object, Object> entry : fieldMapValue.entrySet()) {
                    String key = conversionService.convert(entry.getKey(), String.class);
                    String value = conversionService.convert(entry.getValue(), String.class);
                    // 添加配置
                    String mapTargetConfigPath = StrUtil.endWith(targetConfigPath, CONFIG_SPLIT) ? targetConfigPath + key : targetConfigPath + CONFIG_SPLIT + key;
                    mappings.add(new OverrideMapping(originalConfigPath, mapTargetConfigPath, value));
                }
            } else {
                property = conversionService.convert(ReflectUtil.getFieldValue(overrideObject, field), String.class);
                // 如果配置还是空的话，则不进行处理
                if (StrUtil.isBlank(property)) {
                    continue;
                }
                mappings.add(new OverrideMapping(originalConfigPath, targetConfigPath, property));
            }
        }
        return mappings;
    }

    /**
     * 处理类中标记了@OverrideProperty和@NestedConfigurationProperty的字段
     */
    private List<OverrideMapping> acquireOverrideMappings(ConfigurableEnvironment environment, String originalPrefix, String targetPrefix, Class<?> overrideClass) {
        List<OverrideMapping> mappings = new ArrayList<>();
        // 将可以转换的配置先进行转换
        if (environment.getProperty(originalPrefix) != null) {
            mappings.add(new OverrideMapping(originalPrefix, targetPrefix, environment.getProperty(originalPrefix)));
        }
        // 实例化类
        Object overrideObject;
        try {
            overrideObject = overrideClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EnvironmentInstanceException("实例重写配置对象：{} 失败", e, overrideClass);
        }
        // 处理字段上标注了OverrideProperty的注解
        Field[] declaredFields = overrideClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 获取注解
            OverrideProperty overrideProperty = AnnotationUtils.findAnnotation(field, OverrideProperty.class);
            if (overrideProperty == null) {
                continue;
            }
            // 处理具体字段
            mappings.addAll(this.overrideFieldHandler(field, overrideObject, overrideProperty, originalPrefix, targetPrefix, environment));
        }
        return mappings;
    }

    private List<OverrideMapping> overrideFieldHandler(Field field, Object overrideObject, OverrideProperty overrideProperty, String originalPrefix, String targetPrefix, ConfigurableEnvironment environment) {
        ConfigurableConversionService conversionService = environment.getConversionService();
        // 默认值
        Object defaultValue = ReflectUtil.getFieldValue(overrideObject, field);
        // 是不是spring支持的类型
        if (!conversionService.canConvert(String.class, defaultValue.getClass())) {
            // todo 使用内置方式进行处理
            throw new UnsupportedOperationException("目前不支持非spring支持的基本对象");
        }
        List<OverrideMapping> mappings = new ArrayList<>();
        // 转换默认值
        String defaultValueStr = conversionService.convert(defaultValue, String.class);
        if (StrUtil.isBlank(defaultValueStr)) {
            defaultValueStr = "";
        }
        originalPrefix = StrUtil.endWith(originalPrefix, CONFIG_SPLIT) ? originalPrefix : originalPrefix + CONFIG_SPLIT;
        // 获取注解的源配置路径
        String originalConfigPath = originalPrefix + this.convertFieldName2ConfigName(field.getName());
        // 替换目标配置路径
        String targetConfigPath = overrideProperty.target();
        // 获取配置值
        String property = environment.getProperty(originalConfigPath, defaultValueStr);
        mappings.add(new OverrideMapping(originalConfigPath, targetConfigPath, property));
        return mappings;
    }

    /**
     * 将驼峰转换为"-"模式
     */
    private String convertFieldName2ConfigName(String fieldName) {
        fieldName = fieldName.replaceAll("([A-Z])", "-$1").toLowerCase();
        return StrUtil.startWith(fieldName, "-") ? StrUtil.subSuf(fieldName, 1) : fieldName;
    }

    private String getSpringPropertyPrefix(Class<?> overrideClass) {
        // 获取spring的配置注解
        ConfigurationProperties configurationProperties = AnnotationUtils.findAnnotation(overrideClass, ConfigurationProperties.class);
        if (configurationProperties == null) {
            throw new EnvironmentConfigException("资源:{} 获取注解@ConfigurationProperties失败,在使用@OverrideClass没有使用@ConfigurationProperties注解", overrideClass);
        }
        String prefix = configurationProperties.prefix();
        return StrUtil.isBlank(prefix) ? "" : prefix;
    }


}
