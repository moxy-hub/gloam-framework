package com.gloamframework.property;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.conversion.MappingPropertyDefinitionConversion;
import com.gloamframework.property.exception.MappingPropertyException;
import org.apache.commons.logging.Log;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashSet;
import java.util.Set;

/**
 * * gloam映射执行类
 * * 支持包扫描，主要定义对象的生成交与{@link com.gloamframework.property.conversion.MappingPropertyDefinitionConversion}进行处理
 *
 * @author 晓龙
 */
public final class DefaultMappingPropertyCollector extends AbstractMappingPropertyCollector {

    /**
     * gloam环境配置名称
     */
    private static final String GLOAM_ENV_NAMESPACE = "gloam-environment";

    /**
     * 配置转换器
     */
    private final MappingPropertyDefinitionConversion conversionService;

    public DefaultMappingPropertyCollector(ConfigurableEnvironment environment, MappingPropertyDefinitionConversion conversionService, ClassLoader classLoader, Log log) {
        super(environment, classLoader, GLOAM_ENV_NAMESPACE, log);
        this.conversionService = conversionService;
    }

    public DefaultMappingPropertyCollector(ConfigurableEnvironment environment, MappingPropertyDefinitionConversion conversionService) {
        this(environment, conversionService, DefaultMappingPropertyCollector.class.getClassLoader(), null);
    }

    @Override
    public Set<MappingPropertyDefinition> collectMappingPropertyDefinitions() {
        Set<MappingPropertyDefinition> definitions = new HashSet<>();
        // 全部拥有@ConfigurationProperties的类
        Set<Class<?>> configurationPropertiesClasses = super.scannerClassWithGloamConfigurationProperties();
        // 遍历重写类，进行映射
        configurationPropertiesClasses.forEach(mappingClass -> {
            // 获取前缀
            ConfigurationProperties configurationProperties = AnnotationUtils.findAnnotation(mappingClass, ConfigurationProperties.class);
            if (configurationProperties == null) {
                throw new MappingPropertyException("资源:{} 获取注解@ConfigurationProperties失败,在使用@MappingConfigurationProperty没有使用@ConfigurationProperties注解", mappingClass);
            }
            String prefix = StrUtil.isBlank(configurationProperties.prefix()) ? "" : configurationProperties.prefix();
            definitions.addAll(super.assembleMappingPropertyDefinition(prefix, null, mappingClass));
        });
        return definitions;
    }

    @Override
    public Set<MappingPropertyDefinition> assembleMappingPropertyDefinition(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultProperty) {
        return conversionService.convert(originalPath, mappingPath, mappingClass, defaultProperty);
    }

}
