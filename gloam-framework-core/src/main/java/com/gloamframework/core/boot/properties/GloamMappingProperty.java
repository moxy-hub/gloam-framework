package com.gloamframework.core.boot.properties;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.properties.conversion.MappingPropertyDefinitionConversion;
import com.gloamframework.core.boot.properties.exception.MappingPropertyException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * gloam映射执行类
 * 支持包扫描，主要定义对象的生成交与{@link MappingPropertyDefinitionConversion}进行处理
 *
 * @author 晓龙
 */
public class GloamMappingProperty extends AbstractMappingProperty {

    /**
     * gloam环境配置名称
     */
    private static final String GLOAM_ENV_NAMESPACE = "gloam-environment";

    /**
     * 配置转换器
     */
    private final MappingPropertyDefinitionConversion conversionService;

    public GloamMappingProperty(ConfigurableEnvironment environment, MappingPropertyDefinitionConversion conversionService, ClassLoader classLoader) {
        super(environment, classLoader, GLOAM_ENV_NAMESPACE);
        this.conversionService = conversionService;
    }

    public GloamMappingProperty(ConfigurableEnvironment environment, MappingPropertyDefinitionConversion conversionService) {
        this(environment, conversionService, GloamMappingProperty.class.getClassLoader());
    }

    @Override
    public List<MappingPropertyDefinition> collectMappingPropertyDefinitions(String... packagePath) {
        List<MappingPropertyDefinition> definitions = new ArrayList<>();
        if (ArrayUtil.isEmpty(packagePath)) {
            return definitions;
        }
        // 全部拥有@ConfigurationProperties的类
        List<Class<?>> configurationPropertiesClasses = super.scannerClassWithConfigurationProperties(packagePath);
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
    public List<MappingPropertyDefinition> assembleMappingPropertyDefinition(String originalPath, String mappingPath, Class<?> mappingClass, Object defaultProperty) {
        return conversionService.convert(originalPath, mappingPath, mappingClass, defaultProperty);
    }
}
