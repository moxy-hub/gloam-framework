package com.gloamframework.web.debouncing.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 晓龙
 * 接口防抖注解
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Debouncing {

    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    int interval() default 10000;

    /**
     * 提示消息
     */
    String message() default "您已提交，请稍候再试";

    /**
     * 是否限制登录的token，如果关闭后，则为请求地址加ip的判断
     */
    boolean tokenLimit() default true;

    /**
     * 是否开启Ip限制，默认开启
     */
    boolean enableIp() default true;

}
