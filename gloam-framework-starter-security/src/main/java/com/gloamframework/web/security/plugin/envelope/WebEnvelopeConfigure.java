package com.gloamframework.web.security.plugin.envelope;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 */
@Configurable
public class WebEnvelopeConfigure {

    @Bean
    public WebEnvelopeMatcher webEnvelopeMatcher() {
        return new WebEnvelopeMatcher();
    }

    @Bean
    public WebEnvelopeFilter webEnvelopeFilter() {
        return new WebEnvelopeFilter();
    }

}
