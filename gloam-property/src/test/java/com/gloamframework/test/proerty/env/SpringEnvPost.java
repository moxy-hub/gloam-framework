package com.gloamframework.test.proerty.env;

import com.gloamframework.property.DefaultPropertyMapper;
import com.gloamframework.property.PropertyMapper;
import com.gloamframework.property.PropertyMapperCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 使用spring的EnvironmentPostProcessor进行处理，因为我们映射的是spring的配置，优先级要在处理环境时进行
 * 由于在环境初始化时，日志系统还没有准备好，我们又希望将日志输出，所以使用springboot为我们提供的延迟日志
 * 配合spring的事件系统，在监听到环境准备完成的事件后，对我们的日志进行回放
 */
public class SpringEnvPost implements EnvironmentPostProcessor, ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    /**
     * spring boot延迟日志
     */
    private static final DeferredLog deferredLog = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 实例化配置映射，将日志传入用于内部日志的打印，将spring的环境传入，命名映射后配置在环境中存储的namespace，传入类加载器
        PropertyMapperCollector propertyMapper = new DefaultPropertyMapper(deferredLog, environment, "test-mapping", application.getClassLoader());
        // 执行映射
        propertyMapper.mapping(propertyMapper.collectMappingPropertyDefinitions());
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 在监听到环境准备就绪事件后进行日志回放
        deferredLog.replayTo(SpringEnvPost.class);
    }
}
