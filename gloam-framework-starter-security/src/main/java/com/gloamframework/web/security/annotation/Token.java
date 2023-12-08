package com.gloamframework.web.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识了当前注解，标识请求不需要携带token即可访问，主要用于登录认证请求
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Token {

    enum Strategy {
        /**
         * 不需要携带token可以访问
         */
        NONE,

        /**
         * 想要携带token，如果有则会进行解析，用于无需token的情况，但是要获取登录的用户，那么传入的token将会解析为登录的用户
         */
        WANT,

        /**
         * 当前请求需要token，必须要求携带，默认策略，不加当前注解时，默认的策略即为NEED
         */
        NEED

    }

    /**
     * token的携带处理策略
     */
    Strategy strategy() default Strategy.NEED;
}
