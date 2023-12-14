package com.gloamframework.web.security.plugin.xss;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author 晓龙
 */
@Configurable
public class XssSecurityConfigure {

    @Bean
    public XssSecurityFilter xssSecurityFilter() {
        return new XssSecurityFilter();
    }

}
