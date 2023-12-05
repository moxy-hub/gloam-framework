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

    /**
     * 访问需要的权限，不填则表示没有权限认证
     */
    String[] hasAuth() default {};

    /**
     * 访问需要的角色，不填则表示没有角色认证
     */
    String[] hasRole() default {};

}
