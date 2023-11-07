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
     * 监控用户名
     */
    String username() default "admin";

    /**
     * 监控密码
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
