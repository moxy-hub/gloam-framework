package com.gloamframework.web.envelope.rsa;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 */
@Configurable
@EnableConfigurationProperties(RsaProperties.class)
public class RsaSecurityConfigure {

    @Bean
    public RsaService rsaService() {
        return new RsaService();
    }

}
