package com.gloamframework.core.logging.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.util.unit.DataSize;

/**
 * 日志配置的嵌套配置
 *
 * @author 晓龙
 */
@Data
public class LoggingFileProperties {

    /**
     * 指定日志文件的路径与名字(例C://logs/info.log)
     * 日志文件的名字是info.log
     */
    @MappingConfigurationProperty("name")
    private String name;

    /**
     * 指定日志文件的路径，不能指定名字(例C://logs)
     * 日志文件的名字是默认的spring.log
     */
    @MappingConfigurationProperty("path")
    private String path;

    /**
     * 设置日志保留的天数。如果clean-history-on-start设置为true，则将删除旧文件,gloam默认14天
     */
    @MappingConfigurationProperty("max-history")
    private Integer maxHistory = 14;

    /**
     * 是否在启动时清除存档日志文件
     */
    @MappingConfigurationProperty("clean-history-on-start")
    private Boolean cleanHistoryOnStart;

    /**
     * 设置一个文件的最大大小，如果超过该文件大小，将创建一个新文件,gloam默认50MB
     */
    @MappingConfigurationProperty("max-size")
    private DataSize maxSize = DataSize.ofMegabytes(50);

    /**
     * 设置所有日志文件的总大小限制,默认100MB
     */
    @MappingConfigurationProperty("total-size-cap")
    private DataSize totalSizeCap = DataSize.ofMegabytes(100);

}
