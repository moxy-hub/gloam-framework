package com.gloamframework.property;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.gloamframework.property.annotation.GloamConfigurationProperties;
import com.gloamframework.property.exception.MappingPropertyScannerException;
import com.gloamframework.scanner.ResourceCentre;
import com.gloamframework.scanner.ResourceCentreFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 处理映射
 *
 * @author 晓龙
 */
abstract class AbstractMappingPropertyCollector implements MappingPropertyCollector {

    /**
     * gloam log
     */
    protected final Log log;

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

    protected AbstractMappingPropertyCollector(ConfigurableEnvironment environment, ClassLoader classLoader, String environmentNamespace, Log log) {
        this.environment = environment;
        this.classLoader = Objects.isNull(classLoader) ? ClassLoader.getSystemClassLoader() : classLoader;
        this.environmentNamespace = environmentNamespace;
        this.log = Objects.isNull(log) ? LogFactory.getLog(AbstractMappingPropertyCollector.class) : log;
    }

    /**
     * 扫描获取标注了@ConfigurationProperties注解的类
     */
    protected Set<Class<?>> scannerClassWithGloamConfigurationProperties() {
        // 扫描符合要求的包，OverrideClass标注的类
        try {
            ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault(classLoader, log);
            return resourceCentre.getResourcesClassesByAnnotation(GloamConfigurationProperties.GloamConfigurationResource, GloamConfigurationProperties.class);
        } catch (IOException e) {
            throw new MappingPropertyScannerException("获取资源失败", "配置重写映射失败", e);
        }
    }

    @Override
    public void mapping() {
        Set<MappingPropertyDefinition> mappingPropertyDefinitions = this.collectMappingPropertyDefinitions();
        if (CollectionUtil.isEmpty(mappingPropertyDefinitions)) {
            log.info("No mappingPropertyDefinition found,skip mapping");
            return;
        }
        log.debug("Start mapping configuration properties whit annotation @MappingConfigurationProperty");
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
            if (StrUtil.isNotBlank(value)) {
                mapPropertySource.put(definition.getMappingPath(), value);
                log.trace(StrUtil.format("Mapping configuration property -> original:[{}] - mapping:[{}] - value:[{}]", definition.getOriginalPath(), definition.getMappingPath(), value));
            }
        });

    }
}
