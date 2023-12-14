package com.gloamframework.cloud.sentinel.properties.log;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

/**
 * @author 晓龙
 */
@Data
public class SentinelLogProperties {

    /**
     * sentinel日志
     */
    @MappingConfigurationProperty("dir")
    private String dir = "./logs/sentinel/";

    /**
     * 是否启动pid
     */
    @MappingConfigurationProperty("switch-pid")
    private boolean switchPid = false;

}
