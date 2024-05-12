package com.gloamframework.property;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射抽象类，主要负责将参数在spring的环境中进行替换
 *
 * @author 晓龙
 */
@AllArgsConstructor
public abstract class AbstractPropertyMapper implements PropertyMapperCollector {

    /**
     * log:外部注入的日志系统，处理延迟日志
     */
    protected final Log log;

    /**
     * spring environment
     */
    private final ConfigurableEnvironment environment;

    /**
     * 环境配置名称
     */
    private final String environmentNamespace;

    @Override
    public void mapping() {
        Set<PropertyMapperDefinition> mappingPropertyDefinitions = this.collectMappingPropertyDefinitions();
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
