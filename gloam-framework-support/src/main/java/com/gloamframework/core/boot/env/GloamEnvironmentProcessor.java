package com.gloamframework.core.boot.env;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.gloamframework.common.lang.StringUtil;
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

    private static final String GLOAM_ENV_NAME_PATTERN = "GLOAM_ENV_{}";
    private static volatile boolean startup = false;
    private static volatile boolean initMapped = false;

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
        return 0;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        /*
         * 由于多种环境，会导致这里的环境进行两次调用，导致gloom启动了两次，必须要保证各种环境中正确的处理配置文件，所以在每中环境中都需要加载一份配置文件
         */
        // 如果没启动过，就进行启动
        String springEnvName = this.checkStartup() ? null : this.initGloam(application);
        springEnvName = StringUtil.isNotBlank(springEnvName) ? springEnvName : GloamContext.getOrDefaultByFunction(GloamKeys.GLOAM_ENV_NAME_SPACE, () -> {
            Class<?> mainApplicationClass = application.getMainApplicationClass();
            return StringUtil.format(GLOAM_ENV_NAME_PATTERN, Objects.nonNull(mainApplicationClass) ? mainApplicationClass.getSimpleName() : IdUtil.fastUUID());
        });
        PropertyMapperCollector propertyMapper = new DefaultPropertyMapper(log, environment, springEnvName, application.getClassLoader());
        GloamContext.put(GloamKeys.GLOAM_ENV_NAME_SPACE, springEnvName);
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
        if (!initMapped) {
            log.replayTo(GloamEnvironmentProcessor.class);
            initMapped = true;
        }
    }

    private String initGloam(SpringApplication application) {
        log.info("Welcome to use gloam framework");
        // 初始化资源扫描
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (Objects.isNull(mainApplicationClass)) {
            return StringUtil.format(GLOAM_ENV_NAME_PATTERN, IdUtil.fastUUID());
        }
        // 添加项目启动路径
        ResourcePackagesRegister.registerPackages(mainApplicationClass.getPackage().getName());
        return StringUtil.format(GLOAM_ENV_NAME_PATTERN, application.getMainApplicationClass().getSimpleName());
    }

}
