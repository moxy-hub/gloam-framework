package com.gloamframework.core.web.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.util.unit.DataSize;

/**
 * 响应压缩配置
 *
 * @author 晓龙
 */
@Data
public class CompressionProperties {

    /**
     * 响应压缩，压缩后减少带宽占用，默认开启
     */
    @MappingConfigurationProperty("enabled")
    private boolean enabled = true;

    /**
     * 支持压缩的响应类型
     */
    @MappingConfigurationProperty("mime-types")
    private String[] mimeTypes = new String[]{"text/html", "text/xml", "text/plain", "text/css", "text/javascript",
            "application/javascript", "application/json", "application/xml"};

    /**
     * 指定不压缩的user-agent,使用正则表达式指定哪些浏览器不压缩
     */
    @MappingConfigurationProperty("excluded-user-agents")
    private String[] excludedUserAgents;

    /**
     * 压缩阈值，响应达到多少后才进行压缩，默认15KB
     */
    @MappingConfigurationProperty("min-response-size")
    private DataSize minResponseSize = DataSize.ofKilobytes(15);

}
