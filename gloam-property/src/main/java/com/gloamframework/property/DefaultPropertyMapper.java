package com.gloamframework.property;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.annotation.GloamConfigurationProperties;
import com.gloamframework.property.convertor.PropertyConvertorFactory;
import com.gloamframework.property.exception.MappingPropertyException;
import com.gloamframework.property.exception.MappingPropertyScannerException;
import com.gloamframework.property.path.DefaultPropertyPathAssembler;
import com.gloamframework.property.path.PropertyPathAssembler;
import com.gloamframework.scanner.ResourceCentre;
import com.gloamframework.scanner.ResourceCentreFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 默认的配置映射实现，主要负责集成内部path映射和value转换
 *
 * @author 晓龙
 */
public class DefaultPropertyMapper extends AbstractPropertyMapper {

    /**
     * 类加载器
     */
    private final ClassLoader classLoader;

    /**
     * 配置路径装配器
     */
    private final PropertyPathAssembler propertyPathAssembler;

    /**
     * 配置值转换工厂
     */
    private final PropertyConvertorFactory propertyConvertorFactory;

    public DefaultPropertyMapper(Log log, ConfigurableEnvironment environment, String environmentNamespace, ClassLoader classLoader) {
        super(getValidLog(log), environment, environmentNamespace);
        this.classLoader = Objects.isNull(classLoader) ? ClassLoader.getSystemClassLoader() : classLoader;
        this.propertyPathAssembler = new DefaultPropertyPathAssembler();
        this.propertyConvertorFactory = new PropertyConvertorFactory(environment, classLoader, getValidLog(log));
    }

    /**
     * 获取一个有效的日志对象，防止外部实例化时传入空的日志对象
     */
    private static Log getValidLog(Log log) {
        return Objects.isNull(log) ? LogFactory.getLog(DefaultPropertyMapper.class) : log;
    }

    /**
     * 实现配置收集方法，集成内部的路径装配器和转换工厂，对定义对象进行处理
     */
    @Override
    public Set<PropertyMapperDefinition> collectMappingPropertyDefinitions() {
        Set<PropertyMapperDefinition> definitions = new HashSet<>();
        // 收集注解标识到到配置类
        Set<Class<?>> classWithGloamConfigurationProperties = this.scannerClassWithGloamConfigurationProperties();
        // 遍历重写类，进行映射
        classWithGloamConfigurationProperties.forEach(mappingClass -> {
            // 检查spring配置类的注解
            ConfigurationProperties configurationProperties = AnnotationUtils.findAnnotation(mappingClass, ConfigurationProperties.class);
            if (configurationProperties == null) {
                throw new MappingPropertyException("资源:{} 获取注解@ConfigurationProperties失败,在使用@MappingConfigurationProperty没有使用@ConfigurationProperties注解", mappingClass);
            }
            // 获取前缀
            String prefix = StrUtil.isBlank(configurationProperties.prefix()) ? "" : configurationProperties.prefix();
            // 匹配标识到的配置类路径
            propertyPathAssembler.assemblePath(
                    prefix,
                    null,
                    mappingClass,
                    (originPropertyPath, mappingProperPath, propertyType, defaultFieldValue, mappingConfigurationProperty) -> {
                        if (propertyConvertorFactory.canConvert(propertyType)) {
                            // 转换配置值
                            definitions.addAll(propertyConvertorFactory.convert(originPropertyPath, mappingProperPath, propertyType, defaultFieldValue, mappingConfigurationProperty));
                        } else {
                            log.warn("Un support type to convert for type:" + propertyType + "; mappingProperPath->" + mappingProperPath);
                        }
                    });
        });
        return definitions;
    }

    /**
     * 接入资源中心，获取添加了对应注解的资源
     */
    private Set<Class<?>> scannerClassWithGloamConfigurationProperties() {
        // 扫描符合要求的包
        try {
            ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault(classLoader, log);
            return resourceCentre.getResourcesClassesByAnnotation(GloamConfigurationProperties.GloamConfigurationResource, GloamConfigurationProperties.class);
        } catch (IOException e) {
            throw new MappingPropertyScannerException("获取资源失败", "配置重写映射失败", e);
        }
    }
}
