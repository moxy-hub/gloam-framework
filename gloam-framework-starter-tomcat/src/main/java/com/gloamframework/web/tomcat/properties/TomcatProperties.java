package com.gloamframework.web.tomcat.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * tomcat配置重写
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.web.tomcat")
@Data
public class TomcatProperties {

    private static final int MAX_PROCESSORS = Runtime.getRuntime().availableProcessors() * 200;

    /**
     * uri编码，默认UTF-8
     */
    @MappingConfigurationProperty("server.tomcat.uri-encoding")
    private Charset uriEncoding = StandardCharsets.UTF_8;

    /**
     * 等待队列的最大队列长度,默认为系统核心数*200+150
     */
    @MappingConfigurationProperty("server.tomcat.accept-count")
    private int acceptCount = MAX_PROCESSORS + 150;

    /**
     * 最大链接数,默认为acceptCount加threads-max的4倍
     */
    @MappingConfigurationProperty("server.tomcat.max-connections")
    private int maxConnections = (acceptCount + MAX_PROCESSORS) * 4;

    /**
     * 连接超时时间,默认20秒
     */
    @MappingConfigurationProperty("server.tomcat.connection-timeout")
    private Duration connectionTimeout = Duration.ofMillis(20000);

    /**
     * 线程配置
     */
    @MappingConfigurationProperty("server.tomcat.threads")
    @NestedConfigurationProperty
    private Threads threads = new Threads();

    @Data
    public static class Threads {

        /**
         * 最大工作线程数，默认系统核心数*200
         */
        @MappingConfigurationProperty("max")
        private int max = MAX_PROCESSORS;

        /**
         * 最小备用线程数，tomcat启动时的初始化的线程数，默认为最大工作线程除以10
         */
        @MappingConfigurationProperty("min-spare")
        private int minSpare = max / 10;

    }

}
