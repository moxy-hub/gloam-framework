package com.gloamframework.core.boot.env;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.env.exception.EnvironmentConfigException;
import com.gloamframework.core.boot.env.exception.EnvironmentInstanceException;
import com.gloamframework.core.boot.exception.GloamRuntimeException;
import com.gloamframework.core.boot.map.TernaryHashMap;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
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
 * Gloam环境后置处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

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

    /**
     * 设置环境设置触发优先级
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

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
            mappings.addAll(this.acquireOverrideMappings(environment, springConfigPrefix, null, overrideClass, null));
        }
        return mappings;
    }

    /**
     * 处理类中标记了@OverrideProperty和@NestedConfigurationProperty的字段
     */
    private List<OverrideMapping> acquireOverrideMappings(ConfigurableEnvironment environment, String originalPrefix, String targetPrefix, Class<?> overrideClass, Object defaultProperty) {
        List<OverrideMapping> mappings = new ArrayList<>();
        ConfigurableConversionService conversionService = environment.getConversionService();
        // 检查传入的配置类型spring是否可以直接解析
        if (conversionService.canConvert(String.class, overrideClass)) {
            String defaultValue = null;
            // 转换默认值
            if (defaultProperty != null && conversionService.canConvert(String.class, defaultProperty.getClass())) {
                defaultValue = conversionService.convert(defaultProperty, String.class);
            }
            if (StrUtil.isBlank(defaultValue)) {
                defaultValue = "";
            }
            // 在spring 环境中拿值
            String property = environment.getProperty(originalPrefix, defaultValue);
            if (StrUtil.isBlank(property)) {
                return mappings;
            }
            mappings.add(new OverrideMapping(originalPrefix, targetPrefix, property));
            return mappings;
        }
        // 处理配置路径
        if (StrUtil.isNotBlank(originalPrefix)) {
            originalPrefix = StrUtil.endWith(originalPrefix, CONFIG_SPLIT) ? originalPrefix : originalPrefix + CONFIG_SPLIT;
        }
        if (StrUtil.isNotBlank(targetPrefix)) {
            targetPrefix = StrUtil.endWith(targetPrefix, CONFIG_SPLIT) ? targetPrefix : targetPrefix + CONFIG_SPLIT;
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
            this.overrideFieldInjectSpringSupport(environment, originalPrefix, targetPrefix, field, overrideObject, overrideProperty, mappings);
        }
        return mappings;
    }

    /**
     * 重写字段的注入
     */
    private void overrideFieldInjectSpringSupport(ConfigurableEnvironment environment, String originalPrefix, String targetPrefix, Field field, Object overrideObject, OverrideProperty overrideProperty, List<OverrideMapping> mappings) {
        ConfigurableConversionService conversionService = environment.getConversionService();
        // 处理路径
        String originPropertyPath = originalPrefix + this.convertFieldName2ConfigName(field.getName());
        // 目标路径
        String targetProperPath = StrUtil.isBlank(targetPrefix) ? overrideProperty.target() : targetPrefix + overrideProperty.target();
        // 判断字段类型，对map，collection，和穿透型对象进行处理
        Class<?> propertyType = field.getType();
        if (Map.class.isAssignableFrom(propertyType) || Collection.class.isAssignableFrom(propertyType) || propertyType.isArray()) {
            this.overrideFieldGloamSupport(environment, originPropertyPath, targetProperPath, field, overrideObject, overrideProperty, mappings);
            return;
        }
        if (AnnotationUtils.findAnnotation(field, NestedConfigurationProperty.class) != null) {
            mappings.addAll(this.acquireOverrideMappings(environment, originPropertyPath, targetProperPath, propertyType, null));
            return;
        }
        // 默认值
        String defaultValue = conversionService.convert(ReflectUtil.getFieldValue(overrideObject, field), String.class);
        if (StrUtil.isBlank(defaultValue)) {
            defaultValue = "";
        }
        // 获取配置值
        String property = environment.getProperty(originPropertyPath, defaultValue);
        mappings.add(new OverrideMapping(originPropertyPath, targetProperPath, property));
    }

    /**
     * 处理非spring默认支持的字段类型
     * 目前只处理三种
     * MAP类型、Collection类型、Array类型
     */
    private void overrideFieldGloamSupport(ConfigurableEnvironment environment, String originPropertyPath, String targetProperPath, Field field, Object overrideObject, OverrideProperty overrideProperty, List<OverrideMapping> mappings) {
        /*
         * 主要解决的问题是将特殊类型的路径进行拼接
         * 1、循环本地的配置，将路径和默认值存储
         * 2、循环spring环境配置，将路径存储
         */
        // 判断类型
        Class<?> nestedPropertyType = overrideProperty.nestedPropertyType();
        if (nestedPropertyType == null) {
            nestedPropertyType = OverrideProperty.NoNestedProperty.class;
        }
        if (OverrideProperty.NoNestedProperty.class.isAssignableFrom(nestedPropertyType)) {
            nestedPropertyType = String.class;
        } else {
            if (AnnotationUtils.findAnnotation(nestedPropertyType, NestedOverrideClass.class) == null) {
                throw new EnvironmentConfigException("配置环境错误,字段:{},指定类型为{},在类{}上没有识别到@Overclass注解", field.getName(), nestedPropertyType, nestedPropertyType);
            }
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
                if (!StrUtil.startWith(propertyName, originPropertyPath)) {
                    continue;
                }
                // 处理目标路径
                String mapConfig = propertyName.replaceAll(originPropertyPath, "");
                if (StrUtil.isBlank(mapConfig)) {
                    continue;
                }
                int index;
                if ((index = mapConfig.lastIndexOf(CONFIG_SPLIT)) != -1) {
                    mapConfig = mapConfig.substring(0, index);
                }
                String origin = originPropertyPath + mapConfig;
                String target = targetProperPath + mapConfig;
                pathDefaultPropertyMappings.put(origin, target, null);
            }
        }
        // 获取对象默认值
        Object fieldValue = ReflectUtil.getFieldValue(overrideObject, field);
        ConfigurableConversionService conversionService = environment.getConversionService();
        if (fieldValue != null) {
            // 处理map
            if (Map.class.isAssignableFrom(field.getType())) {
                Map<?, ?> mapValue = (Map<?, ?>) fieldValue;
                mapValue.forEach((k, v) -> {
                    if (!conversionService.canConvert(String.class, k.getClass())) {
                        throw new EnvironmentConfigException("环境配置使用MAP字段:{}时，无法正确解析MAP的key类型:{}", field.getName(), k.getClass());
                    }
                    String currentPropertyPath = conversionService.convert(k, String.class);
                    // 拼接原始路径
                    String origin = originPropertyPath + CONFIG_SPLIT + currentPropertyPath;
                    // 拼接目标路径
                    String target = targetProperPath + CONFIG_SPLIT + currentPropertyPath;
                    if (String.class.isAssignableFrom(finalNestedPropertyType)) {
                        if (!conversionService.canConvert(String.class, v.getClass())) {
                            throw new EnvironmentConfigException("配置环境不支持的对象解析:{}", v.getClass());
                        }
                        pathDefaultPropertyMappings.put(origin, target, conversionService.convert(v, String.class));
                    } else {
                        pathDefaultPropertyMappings.put(origin, target, null);
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
                        String origin = originPropertyPath + "[" + index + "]";
                        // 拼接目标路径
                        String target = targetProperPath + "[" + index + "]";
                        if (String.class.isAssignableFrom(finalNestedPropertyType)) {
                            if (!conversionService.canConvert(String.class, arrays[index].getClass())) {
                                throw new EnvironmentConfigException("配置环境不支持的对象解析:{}", arrays[index].getClass());
                            }
                            pathDefaultPropertyMappings.put(origin, target, conversionService.convert(arrays[index], String.class));
                        } else {
                            pathDefaultPropertyMappings.put(origin, target, null);
                        }
                    }
                }
            }
        }
        pathDefaultPropertyMappings.forEach((origin, value) -> {
            // 目标值
            String target = pathDefaultPropertyMappings.getFirst(origin);
            // 默认值
            Object defaultProperty = pathDefaultPropertyMappings.getSecond(origin);
            // 递归进行拿值
            mappings.addAll(this.acquireOverrideMappings(environment, origin, target, finalNestedPropertyType, defaultProperty));
        });
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
