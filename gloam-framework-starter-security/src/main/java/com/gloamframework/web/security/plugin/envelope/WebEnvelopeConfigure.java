package com.gloamframework.web.security.plugin.envelope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 */
@Configurable
@Slf4j
public class WebEnvelopeConfigure {

    public WebEnvelopeConfigure() {
        log.info("启动网路信封保护接口，请在需要保护的接口上标注注解@WebEnvelope");
    }

    @Bean
    public WebEnvelopeMatcher webEnvelopeMatcher() {
        return new WebEnvelopeMatcher();
    }

    @Bean
    public WebEnvelopeFilter webEnvelopeFilter() {
        return new WebEnvelopeFilter();
    }

}
