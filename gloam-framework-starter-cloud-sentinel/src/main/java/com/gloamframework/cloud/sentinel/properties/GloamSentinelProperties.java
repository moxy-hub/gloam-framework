package com.gloamframework.cloud.sentinel.properties;

import com.gloamframework.cloud.sentinel.properties.limit.LimitRuleProperties;
import com.gloamframework.cloud.sentinel.properties.log.SentinelLogProperties;
import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * sentinel配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.cloud.sentine")
@Data
public class GloamSentinelProperties {

    /**
     * 限流配置
     */
    @NestedConfigurationProperty
    private final LimitRuleProperties limit = new LimitRuleProperties();

    @MappingConfigurationProperty("spring.cloud.sentinel.log")
    @NestedConfigurationProperty
    private final SentinelLogProperties log = new SentinelLogProperties();
}
