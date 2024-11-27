package com.gloamframework.core.boot.env;

import cn.hutool.core.collection.CollectionUtil;
import com.gloamframework.core.boot.GloamContext;
import com.gloamframework.core.boot.GloamKeys;
import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.property.DefaultPropertyMapper;
import com.gloamframework.property.PropertyMapperCollector;
import com.gloamframework.property.PropertyMapperDefinitionSet;
import com.gloamframework.scanner.ResourcePackagesRegister;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

/**
 * Gloam环境处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentProcessor implements EnvironmentPostProcessor, Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static volatile boolean startup = false;
    /**
     * 延迟log
     */
    private static final DeferredLog log = new DeferredLog();
    /**
     * gloam banner
     */
    private static final Banner GLOAM_BANNER = new GloamBanner();

    /**
     * 设置环境设置触发优先级
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        /*
         * 由于多种环境，会导致这里的环境进行两次调用，导致gloom启动了两次，必须要保证各种环境中正确的处理配置文件，所以在每中环境中都需要加载一份配置文件
         */
        if (!this.checkStartup()) {
            // 如果没启动过，就进行启动
            this.initGloam(application);
        }
        String springEnvName = environment.getClass().getName();
        PropertyMapperCollector propertyMapper = new DefaultPropertyMapper(log, environment, "gloam-env-4-" + springEnvName, application.getClassLoader());
        log.trace("Create PropertyMapper with env:" + springEnvName);
        // 执行映射
        PropertyMapperDefinitionSet mappingPropertyDefinitions = propertyMapper.collectMappingPropertyDefinitions();
        if (CollectionUtil.isEmpty(mappingPropertyDefinitions)) {
            log.info("No mappingPropertyDefinition found,skip mapping");
            return;
        }
        // 在上下中将定义存下来
        GloamContext.put(GloamKeys.MAPPING_DEFINITIONS_KEY, mappingPropertyDefinitions);
        log.debug("Start mapping configuration properties whit annotation @MappingConfigurationProperty");
        propertyMapper.mapping(mappingPropertyDefinitions);
    }

    private synchronized boolean checkStartup() {
        if (!startup) {
            startup = true;
            return false;
        }
        return startup;
    }

    /**
     * 回放日志
     */
    @Override
    public void onApplicationEvent(@SuppressWarnings("unused") ApplicationEnvironmentPreparedEvent event) {
        log.replayTo(GloamEnvironmentProcessor.class);
    }

    private void initGloam(SpringApplication application) {
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        log.info("Welcome to use gloam framework");
        // 初始化资源扫描
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (Objects.nonNull(mainApplicationClass)) {
            // 添加项目启动路径
            ResourcePackagesRegister.registerPackages(mainApplicationClass.getPackage().getName());
        }
    }
}
