package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.env.convert.GloamConverterDiscover;
import com.gloamframework.core.boot.properties.GloamMappingProperty;
import com.gloamframework.core.boot.properties.MappingProperty;
import com.gloamframework.core.boot.properties.MappingPropertyDefinition;
import com.gloamframework.core.boot.properties.conversion.GloamMappingPropertyDefinitionConversion;
import com.gloamframework.core.boot.properties.conversion.MappingPropertyDefinitionConversion;
import com.gloamframework.core.logging.GloamLog;
import org.apache.commons.logging.Log;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * Gloam环境后置处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    /**
     * gloam log
     */
    private static final Log log = GloamLog.getLogger();
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
        log.info("welcome to use gloam framework");
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        // 配置转换器
        GloamConverterDiscover gloamConverterDiscover = new GloamConverterDiscover(environment.getConversionService(), application.getClassLoader());
        log.trace("create GloamConverterDiscover with conversionService:" + environment.getConversionService().getClass().getName());
        gloamConverterDiscover.doRegister(ASSEMBLE_BASE_PACKAGE, application.getMainApplicationClass().getPackage().getName());
        // 创建映射对象转换器
        MappingPropertyDefinitionConversion mappingPropertyDefinitionConversion = new GloamMappingPropertyDefinitionConversion(environment);
        log.trace("create GloamMappingPropertyDefinitionConversion with env:" + environment.getClass().getName());
        // 创建映射服务
        MappingProperty mappingProperty = new GloamMappingProperty(environment, mappingPropertyDefinitionConversion, application.getClassLoader());
        log.trace("create GloamMappingProperty with env:" + environment.getClass().getName() + " and classLoader:" + application.getClassLoader().getClass().getName());
        // 收集对应的映射定义
        Set<MappingPropertyDefinition> definitions = mappingProperty.collectMappingPropertyDefinitions(ASSEMBLE_BASE_PACKAGE, application.getMainApplicationClass().getPackage().getName());
        // 执行映射
        mappingProperty.doMapping(definitions);
    }


    /**
     * 回放日志
     */
    @Override
    public void onApplicationEvent(@SuppressWarnings("all") ApplicationEnvironmentPreparedEvent event) {
        GloamLog.replayTo(GloamEnvironmentPostProcessor.class);
    }

}
