package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.properties.GloamMappingProperty;
import com.gloamframework.core.boot.properties.MappingProperty;
import com.gloamframework.core.boot.properties.MappingPropertyDefinition;
import com.gloamframework.core.boot.properties.conversion.GloamMappingPropertyDefinitionConversion;
import com.gloamframework.core.boot.properties.conversion.MappingPropertyDefinitionConversion;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

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
     * gloam扫描基础包
     */
    private static final String ASSEMBLE_BASE_PACKAGE = "com.gloamframework";

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
        // 创建映射对象转换器
        MappingPropertyDefinitionConversion mappingPropertyDefinitionConversion = new GloamMappingPropertyDefinitionConversion(environment);
        // 创建映射服务
        MappingProperty mappingProperty = new GloamMappingProperty(environment, mappingPropertyDefinitionConversion, application.getClassLoader());
        // 收集对应的映射定义
        Set<MappingPropertyDefinition> definitions = mappingProperty.collectMappingPropertyDefinitions(ASSEMBLE_BASE_PACKAGE, application.getMainApplicationClass().getPackage().getName());
        // 执行映射
        mappingProperty.doMapping(definitions);
    }

}
