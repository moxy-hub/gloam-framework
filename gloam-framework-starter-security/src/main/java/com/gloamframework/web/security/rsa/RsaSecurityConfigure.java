package com.gloamframework.web.security.rsa;

import com.gloamframework.web.doc.HttpDocRegister;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 晓龙
 */
@Configurable
@ComponentScan("com.gloamframework.web.security.rsa")
public class RsaSecurityConfigure {

    @Bean
    public RsaService rsaService() {
        return new RsaService();
    }

    @Bean
    public HttpDocRegister rsaDoc() {
        return () -> this.getClass().getPackage();
    }
}
