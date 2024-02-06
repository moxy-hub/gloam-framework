package com.gloamframework.web.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注了该注解的接口方法或类，则表示全部需要认证
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Authentication {

    enum TokenStrategy {
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
     * 访问需要的权限，不填则表示没有权限认证
     */
    String[] hasAuth() default {};

    /**
     * 访问需要的角色，不填则表示没有角色认证
     */
    String[] hasRole() default {};

    /**
     * token的携带处理策略
     */
    TokenStrategy tokenStrategy() default TokenStrategy.NEED;

}
