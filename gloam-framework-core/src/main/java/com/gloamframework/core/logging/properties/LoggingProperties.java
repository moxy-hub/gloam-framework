package com.gloamframework.core.logging.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.logging")
@Data
public class LoggingProperties {

    /**
     * 日志分组
     */
    @MappingConfigurationProperty("logging.group")
    private Map<String, List<String>> group;

    /**
     * 日志级别
     */
    @MappingConfigurationProperty("logging.level")
    private Map<String, LogLevel> level;

    /**
     * 本地日志配置
     */
    @MappingConfigurationProperty("logging.config")
    private String config;

    /**
     * 记录异常时使用的转换字
     */
    @MappingConfigurationProperty("logging.exception-conversion-word")
    private String exceptionConversionWord;

    /**
     * 初始化日志系统时，注册一个关闭挂钩。
     */
    @MappingConfigurationProperty("logging.register-shutdown-hook")
    private Boolean registerShutdownHook;

    /**
     * 日志文件配置
     */
    @MappingConfigurationProperty("logging.file")
    @NestedConfigurationProperty
    private LoggingFileProperties file;

    /**
     * 日志格式配置
     */
    @MappingConfigurationProperty("logging.pattern")
    @NestedConfigurationProperty
    private LoggingPatternProperties pattern;

    public LoggingProperties() {
        level = new HashMap<>();
        // 默认级别
        level.put("com.*", LogLevel.DEBUG);
        level.put("org.*", LogLevel.INFO);
    }
}
