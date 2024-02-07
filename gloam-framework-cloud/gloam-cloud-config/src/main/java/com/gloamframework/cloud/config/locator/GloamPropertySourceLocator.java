package com.gloamframework.cloud.config.locator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;
import com.gloamframework.core.boot.env.GloamEnvironmentPostProcessor;
import com.gloamframework.core.boot.properties.MappingPropertyDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 配置检测
 *
 * @author 晓龙
 */
@Order(1)
@Slf4j
public class GloamPropertySourceLocator extends NacosPropertySourceLocator {

    /**
     * gloam环境配置名称
     */
    private static final String GLOAM_ENV_NAMESPACE = "gloam-environment";

    private final Set<MappingPropertyDefinition> mappingPropertyDefinitions = GloamEnvironmentPostProcessor.definitions;

    public GloamPropertySourceLocator(NacosConfigManager nacosConfigManager) {
        super(nacosConfigManager);
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        CompositePropertySource propertySource = (CompositePropertySource) super.locate(environment);
        if (CollectionUtil.isEmpty(mappingPropertyDefinitions)) {
            return propertySource;
        }
        if (!ConfigurableEnvironment.class.isAssignableFrom(environment.getClass())) {
            return propertySource;
        }
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        MapPropertySource mapPropertySource = (MapPropertySource) configurableEnvironment.getPropertySources().get(GLOAM_ENV_NAMESPACE);
        if (Objects.isNull(mapPropertySource)) {
            return propertySource;
        }
        log.debug("start mapping configuration properties whit annotation @MappingConfigurationProperty");
        final Map<String, Object> mapPropertySourceMap = mapPropertySource.getSource();
        mappingPropertyDefinitions.forEach(definition -> {
            Object value = propertySource.getProperty(definition.getOriginalPath());
            // 这里只处理远程的配置，内部默认值不进行处理
            if (Objects.nonNull(value)) {
                mapPropertySourceMap.put(definition.getMappingPath(), value);
                log.trace(StrUtil.format("mapping configuration property -> original:[{}] - mapping:[{}] - value:[{}]", definition.getOriginalPath(), definition.getMappingPath(), value));
            }
        });
        return propertySource;
    }

}
