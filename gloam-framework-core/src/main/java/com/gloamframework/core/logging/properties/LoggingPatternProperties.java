package com.gloamframework.core.logging.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

/**
 * 日志格式配置
 *
 * @author 晓龙
 */
@Data
public class LoggingPatternProperties {

    /**
     * 文件中使用的日志模式，也就是系统变量FILE_LOG_PATTERN的值
     */
    @MappingConfigurationProperty("console")
    private String consoles;

    /**
     * 配置日志日期格式，也就是系统变量LOG_DATEFORMAT_PATTERN的值
     */
    @MappingConfigurationProperty("dateformat")
    private String dateformat;

    /**
     * 定义文件中日志的样式
     */
    @MappingConfigurationProperty("file")
    private String file;

    /**
     * 定义渲染不同级别日志的格式。默认是%5p
     */
    @MappingConfigurationProperty("level")
    private String level;

    /**
     * 过渡日志文件名的模式。仅默认的Logback设置受支持。
     */
    @MappingConfigurationProperty("rolling-file-name")
    private String rollingFileName;
}
