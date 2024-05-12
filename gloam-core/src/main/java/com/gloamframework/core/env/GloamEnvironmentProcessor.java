package com.gloamframework.core.env;

import com.gloamframework.core.banner.GloamBanner;
import com.gloamframework.property.DefaultPropertyMapper;
import com.gloamframework.property.PropertyMapper;
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
        // 设置banner
        application.setBanner(GLOAM_BANNER);
        if (this.checkStartup()) {
            return;
        }
        log.info("Welcome to use gloam framework");
        // 初始化资源扫描
        Class<?> mainApplicationClass = application.getMainApplicationClass();
        if (Objects.nonNull(mainApplicationClass)) {
            // 添加项目启动路径
            ResourcePackagesRegister.registerPackages(mainApplicationClass.getPackage().getName());
        }
        PropertyMapper propertyMapper = new DefaultPropertyMapper(log, environment, "eee", application.getClassLoader());
        log.trace("Create PropertyMapper with env:" + environment.getClass().getName());
        // 执行映射
        propertyMapper.mapping();
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

}
