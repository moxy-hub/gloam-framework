package com.gloamframework.core.web.undertow.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.unit.DataSize;

/**
 * undertow配置重写
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.web.undertow")
@Data
public class UndertowProperties {

    /**
     * 缓冲池配置
     */
    @NestedConfigurationProperty
    private BufferPool bufferPool = new BufferPool();

    @Data
    public static class BufferPool {

        /**
         * 缓冲池缓冲大小，默认2048b
         */
        private DataSize bufferSize = DataSize.ofBytes(2048);

    }

}
