package com.gloamframework.cloud.sentinel;

import com.gloamframework.cloud.sentinel.exception.GloamSentinelExceptionResolver;
import com.gloamframework.cloud.sentinel.limit.LimitRuleConfigure;
import com.gloamframework.cloud.sentinel.properties.GloamSentinelProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Sentinel配置
 *
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(GloamSentinelProperties.class)
@Import(LimitRuleConfigure.class)
public class SentinelConfigure {

    @Bean
    public GloamSentinelExceptionResolver gloamSentinelExceptionResolver(GloamSentinelProperties gloamSentinelProperties) {
        return new GloamSentinelExceptionResolver(gloamSentinelProperties.getLimit());
    }
}
