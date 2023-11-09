package com.gloamframework.web.security.chain.annotation;

import com.gloamframework.web.security.chain.GloamSecurityConfigurer;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动security chain
 * @author 晓龙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Import(GloamSecurityConfigurer.class)
public @interface EnableSecurityChain {
}
