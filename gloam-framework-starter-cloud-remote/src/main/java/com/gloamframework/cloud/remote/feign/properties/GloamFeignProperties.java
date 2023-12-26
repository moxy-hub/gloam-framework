package com.gloamframework.cloud.remote.feign.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import feign.ExceptionPropagationPolicy;
import feign.Logger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * gloam对feign的基础配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.cloud.feign")
@Data
public class GloamFeignProperties {

    /**
     * 默认的feign配置
     */
    private static final String DEFAULT_FEIGN_CONFIGURATION = "default";

    /**
     * 指定配置文件加载的顺序(全局上下文->默认上下文->指定FeignClient上下文)，默认：true
     * 否则按照 默认上下文->指定FeignClient上下文->全局上下文顺序加载
     */
    @MappingConfigurationProperty("feign.client.default-to-properties")
    private boolean defaultToProperties = true;

    /**
     * Feign Clients contextId 默认配置名
     */
    @MappingConfigurationProperty("feign.client.default-config")
    private String defaultConfig = DEFAULT_FEIGN_CONFIGURATION;

    @NestedConfigurationProperty
    @MappingConfigurationProperty("feign.client.config." + DEFAULT_FEIGN_CONFIGURATION)
    private FeignClientConfiguration defaultClient = new FeignClientConfiguration();
    /**
     * 配置
     */
    @MappingConfigurationProperty(value = "feign.client.config", nestedPropertyType = FeignClientConfiguration.class)
    private Map<String, FeignClientConfiguration> config = new HashMap<>();

    /**
     * 默认开启压缩
     */
    @MappingConfigurationProperty("feign.compression.request.enabled")
    private boolean enableReqGzip = true;

    @MappingConfigurationProperty("feign.compression.response.enabled")
    private boolean enableResGzip = true;

    @MappingConfigurationProperty("feign.compression.response.useGzipDecoder")
    private boolean useGzipDecoder = true;

    @Data
    public static class FeignClientConfiguration {

        /**
         * 默认开启feign的日志
         */
        @MappingConfigurationProperty("logger-level")
        private Logger.Level loggerLevel = Logger.Level.BASIC;

        /**
         * 请求超时时间，默认10秒
         */
        @MappingConfigurationProperty("connect-timeout")
        private Integer connectTimeout = 10 * 1000;

        /**
         * 读取超时时间，默认30秒
         */
        @MappingConfigurationProperty("read-timeout")
        private Integer readTimeout = 30 * 1000;

        /**
         * 对于异常，默认打开，将原因抛出
         */
        @MappingConfigurationProperty("exception-propagation-policy")
        private ExceptionPropagationPolicy exceptionPropagationPolicy = ExceptionPropagationPolicy.UNWRAP;
    }
}
