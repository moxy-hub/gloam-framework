package com.gloamframework.data.druid;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

/**
 * druid监控器注解Selector
 *
 * @author 晓龙
 */
public class DruidMonitorSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        MergedAnnotation<EnableDruidMonitor> annotation = importingClassMetadata.getAnnotations().get(EnableDruidMonitor.class);
        DruidConfigure.username = annotation.getString("username");
        DruidConfigure.password = annotation.getString("password");
        DruidConfigure.deny = annotation.getStringArray("deny");
        DruidConfigure.allow = annotation.getStringArray("allow");
        DruidConfigure.resetEnable = annotation.getBoolean("resetEnable");
        return new String[]{DruidConfigure.class.getName()};

    }
}
