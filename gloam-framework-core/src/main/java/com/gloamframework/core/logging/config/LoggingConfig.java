package com.gloamframework.core.logging.config;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloamframework.logging")
@Data
public class LoggingConfig {

    @MappingConfigurationProperty("logging.level")
    private Map<String, LogLevel> level;

    public LoggingConfig() {
        level = new HashMap<>();
        level.put("com.*", LogLevel.DEBUG);
    }
}
