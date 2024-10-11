package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.convert.GloamConverterDiscover;
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

import java.util.Objects;
import java.util.Set;

/**
 * Gloam环境后置处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered, ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static volatile boolean startup = false;

    /**
     * gloam log
     */
    private static final Log log = GloamLog.getLogger();
    /**
     * gloam banner
     */
    private static final Banner GLOAM_BANNER = new GloamBanner();

    public static Set<MappingPropertyDefinition> definitions = null;

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
        // 配置转换器
        GloamConverterDiscover gloamConverterDiscover = new GloamConverterDiscover(environment.getConversionService(), application.getClassLoader());
        log.trace("create GloamConverterDiscover with conversionService:" + environment.getConversionService().getClass().getName() + " for env:" + environment.getClass());
        // 执行注册
        gloamConverterDiscover.doRegister(GloamAutoScannerPackages.getPackageArrays());
        // 创建映射对象转换器
        MappingPropertyDefinitionConversion mappingPropertyDefinitionConversion = new GloamMappingPropertyDefinitionConversion(environment);
        log.trace("create GloamMappingPropertyDefinitionConversion with env:" + environment.getClass().getName() + " for env:" + environment.getClass());
        // 创建映射服务
        MappingProperty mappingProperty = new GloamMappingProperty(environment, mappingPropertyDefinitionConversion, application.getClassLoader());
        log.trace("create GloamMappingProperty with env:" + environment.getClass().getName() + " and classLoader:" + application.getClassLoader().getClass().getName() + " for env:" + environment.getClass());
        // 收集对应的映射定义
        definitions = mappingProperty.collectMappingPropertyDefinitions(GloamAutoScannerPackages.getPackageArrays());
        // 执行映射
        mappingProperty.doMapping(definitions);
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
    public void onApplicationEvent(@SuppressWarnings("all") ApplicationEnvironmentPreparedEvent event) {
        GloamLog.replayTo(GloamEnvironmentPostProcessor.class);
    }

    private void initGloam(SpringApplication application) {
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        log.info("welcome to use gloam framework");
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (Objects.nonNull(mainApplicationClass)) {
            // 添加项目启动路径
            GloamAutoScannerPackages.addPackage(mainApplicationClass.getPackage().getName());
        }
        // 初始化packages
        GloamAutoScannerPackages.doRegister(application.getClassLoader());
    }

}
