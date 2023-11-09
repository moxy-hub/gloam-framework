package com.gloamframework.web.security.chain;

import lombok.Getter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * security链
 * <p>
 * <li><b>WebSecurity:</b>不仅通过HttpSecurity定义某些请求的安全控制，也通过其他方式定义其他某些请求可以忽略安全控制;
 * <li><b>HttpSecurity:</b>仅用于定义需要安全控制的请求(当然HttpSecurity也可以指定某些请求不需要安全控制）;
 * <li><b>可以认为HttpSecurity是WebSecurity的一部分，WebSecurity是包含HttpSecurity的更大的一个概念。</b>
 *
 * @author 晓龙
 */
@Getter
public class SecurityChain {

    /**
     * spring security web
     * WebSecurity构建目标是整个Spring Security安全过滤器FilterChainProxy
     */
    private WebSecurity webSecurity;

    /**
     * spring security http配置
     * HttpSecurity的构建目标仅仅是FilterChainProxy中的一个SecurityFilterChain
     */
    private HttpSecurity httpSecurity;

    void setWebSecurity(WebSecurity webSecurity) {
        this.webSecurity = webSecurity;
    }

    void setHttpSecurity(HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
    }

}
