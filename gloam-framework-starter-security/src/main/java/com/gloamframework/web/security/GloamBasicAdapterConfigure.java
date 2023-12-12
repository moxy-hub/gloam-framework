package com.gloamframework.web.security;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import com.gloamframework.web.security.adapter.GloamWebSecurityConfigurerAdapter;
import com.gloamframework.web.security.handler.GloamHandlerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author 晓龙
 */
@Configurable
@Import(GloamHandlerConfigure.class)
@Slf4j
public class GloamBasicAdapterConfigure {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;


    /**
     * gloam对于httpSecurity的默认配置
     *
     * @author 晓龙
     */
    @Bean
    @ConditionalOnMissingBean(name = "gloamHttpSecurity")
    public GloamHttpSecurityConfigurerAdapter gloamHttpSecurity() {

        return new GloamHttpSecurityConfigurerAdapter() {
            @Override
            public int getOrder() {
                return 1000;
            }

            @Override
            public void init(HttpSecurity http) throws Exception {
                http
                        // 允许跨域
                        .cors().and()
                        // 关闭预防攻击
                        .csrf().disable()
                        // 禁用Session
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                        .and()
                        .exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                        .and()
                        .servletApi();
                log.debug("gloam configure basic http security:{}", http);
            }
        };
    }

    /**
     * gloam对于webSecurity的默认配置
     *
     * @author 晓龙
     */
    @Bean
    @ConditionalOnMissingBean(name = "gloamWebSecurity")
    public GloamWebSecurityConfigurerAdapter gloamWebSecurity() {
        return new GloamWebSecurityConfigurerAdapter() {
            @Override
            public int getOrder() {
                return 1001;
            }

            @Override
            public void configure(WebSecurity web) {
                // 彻底放行swagger
                web.ignoring().antMatchers(
                        "/doc.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/favicon.ico");
                log.debug("gloam configure basic web security:{}", web);
            }

        };
    }

    @Bean
    public GloamRequestAuthorityFilter gloamRequestAuthorityFilter() {
        return new GloamRequestAuthorityFilter();
    }

}
