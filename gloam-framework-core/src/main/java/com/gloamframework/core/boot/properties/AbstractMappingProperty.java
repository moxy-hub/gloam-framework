package com.gloamframework.core.boot.properties;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.core.boot.properties.exception.MappingPropertyException;
import com.gloamframework.core.boot.properties.exception.MappingPropertyScannerException;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.util.*;

/**
 * 处理映射
 *
 * @author 晓龙
 */
public abstract class AbstractMappingProperty implements MappingProperty {

    /**
     * gloam scanner
     */
    private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();
    /**
     * spring environment
     */
    private final ConfigurableEnvironment environment;
    /**
     * 类加载器
     */
    private final ClassLoader classLoader;
    /**
     * 环境配置名称
     */
    private final String environmentNamespace;

    protected AbstractMappingProperty(ConfigurableEnvironment environment, ClassLoader classLoader, String environmentNamespace) {
        this.environment = environment;
        this.classLoader = classLoader;
        this.environmentNamespace = environmentNamespace;
    }

    /**
     * 扫描获取标注了@ConfigurationProperties注解的类
     */
    protected List<Class<?>> scannerClassWithConfigurationProperties(String... targetPackages) {
        List<Class<?>> classes = new ArrayList<>();
        for (String targetPackage : targetPackages) {
            // 扫描符合要求的包，OverrideClass标注的类
            try {
                classes.addAll(resourceScanner.scannerForClassesWithAnnotation(targetPackage, classLoader, ConfigurationProperties.class));
            } catch (IOException e) {
                throw new MappingPropertyScannerException("获取资源失败", "配置重写映射失败", e);
            }
        }
        return classes;
    }

    @Override
    public void doMapping(Set<MappingPropertyDefinition> mappingPropertyDefinitions) {
        if (mappingPropertyDefinitions == null) {
            throw new MappingPropertyException("映射配置失败，配置定义集合为null");
        }
        MapPropertySource propertySource = (MapPropertySource) environment.getPropertySources().get(environmentNamespace);
        if (propertySource == null) {
            propertySource = new MapPropertySource(environmentNamespace, new HashMap<>());
            environment.getPropertySources().addLast(propertySource);
        }
        // 获取环境中的配置
        final Map<String, Object> mapPropertySource = propertySource.getSource();
        mappingPropertyDefinitions.forEach(definition -> {
            String value = null;
            if (StrUtil.isNotBlank(definition.getValue())) {
                value = definition.getValue();
            }
            if (StrUtil.isBlank(value)) {
                System.out.println(StrUtil.format("mapping configuration property: {} is null,skip this property"));
            } else {
                mapPropertySource.put(definition.getMappingPath(), value);
                System.out.println(StrUtil.format("{}: {} --- [mapping configuration property: {} -> {} = {}]", environmentNamespace, DateUtil.date(), definition.getOriginalPath(), definition.getMappingPath(), value));
            }
        });

    }
}
