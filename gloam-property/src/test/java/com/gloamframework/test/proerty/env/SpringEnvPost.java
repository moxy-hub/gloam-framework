package com.gloamframework.test.proerty.env;

import com.gloamframework.property.DefaultPropertyMapper;
import com.gloamframework.property.PropertyMapperDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Set;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 17:31
 */
public class SpringEnvPost implements EnvironmentPostProcessor, ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    DeferredLog deferredLog = new DeferredLog();
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        DefaultPropertyMapper defaultPropertyMapper = new DefaultPropertyMapper(deferredLog,environment,"s",application.getClassLoader());
        Set<PropertyMapperDefinition> propertyMapperDefinitions = defaultPropertyMapper.collectMappingPropertyDefinitions();
        for (PropertyMapperDefinition propertyMapperDefinition : propertyMapperDefinitions) {
            System.out.println(propertyMapperDefinition);
        }
        defaultPropertyMapper.mapping();
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("propertyMapperDefinitions");
        deferredLog.replayTo(SpringEnvPost.class);
    }
}
