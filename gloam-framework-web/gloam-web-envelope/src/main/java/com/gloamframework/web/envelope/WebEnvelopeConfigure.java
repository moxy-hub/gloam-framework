package com.gloamframework.web.envelope;

import com.gloamframework.web.envelope.rsa.RsaSecurityConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author 晓龙
 */
@Configurable
@Slf4j
@Import(RsaSecurityConfigure.class)
public class WebEnvelopeConfigure {

    public WebEnvelopeConfigure() {
        log.info("[WebEnvelope]:启动网路信封保护接口，请在需要保护的接口上标注注解@WebEnvelope");
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
