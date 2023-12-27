package com.gloamframework.cloud.sentinel;

import com.gloamframework.cloud.sentinel.exception.GloamSentinelExceptionResolver;
import com.gloamframework.cloud.sentinel.properties.GloamSentinelProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 只有在web环境下才会启动的配置，主要防止网关使用时错误，因为spring gateway 是基于webFlux实现不是传承servlet
 *
 * @author 晓龙
 */
@Configurable
@ConditionalOnWebApplication
@ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
public class SentinelWebMvcConfigure {

    @Bean
    public GloamSentinelExceptionResolver gloamSentinelExceptionResolver(GloamSentinelProperties gloamSentinelProperties) {
        return new GloamSentinelExceptionResolver(gloamSentinelProperties.getLimit());
    }

}
