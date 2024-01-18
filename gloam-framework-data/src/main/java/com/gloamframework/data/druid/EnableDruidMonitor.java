package com.gloamframework.data.druid;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否启动druid监控
 *
 * @author 晓龙
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DruidConfigure.class, DruidMonitorSelector.class})
public @interface EnableDruidMonitor {

    /**
     * 是否开启认证，默认开启
     */
    boolean enableAuthentication() default true;

    /**
     * 监控用户名，在认证开启下有效
     */
    String username() default "admin";

    /**
     * 监控密码，在认证开启下有效
     */
    String password() default "admin";

    /**
     * 访问黑名单
     */
    String[] deny() default {};

    /**
     * 访问白名单
     */
    String[] allow() default {};

    /**
     * 是否开启重置
     */
    boolean resetEnable() default false;
}
