package com.gloamframework.web.security.chain.assembler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 对spring security chain进行基础配置
 *
 * @author 晓龙
 */
@Configurable
@Slf4j
public class BasicSecurityChainConfigure {

    @Bean
    @ConditionalOnMissingBean(name = "basicSecurityChainAssembler")
    public SecurityChainAssembler basicSecurityChainAssembler() {
        return securityChain -> {
            // 配置web
            securityChain.getWebSecurity()
                    // 彻底放行swagger
                    .ignoring().antMatchers(
                            "/doc.html",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/v2/**",
                            "/favicon.ico");
            // 配置http
            HttpSecurity httpSecurity = securityChain.getHttpSecurity();
            httpSecurity
                    // 允许跨域
                    .cors().and()
                    // 关闭预防攻击
                    .csrf().disable();
        };
    }

}
