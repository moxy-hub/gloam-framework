package com.gloamframework.web.security.match;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * 匹配配置
 *
 * @author 晓龙
 */
@Configurable
public class GloamMachterConfigure {

    /**
     * 增加对认证注解的匹配支持
     */
    @Bean
    public AuthenticationMatcher authenticationMatcher() {
        return new AuthenticationMatcher();
    }

    @Bean
    public TokenMatcher tokenMatcher() {
        return new TokenMatcher();
    }

}
