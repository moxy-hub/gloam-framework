package com.gloamframework.core.boot.env;

import java.lang.annotation.*;

/**
 * 重写spring配置注解，通过改注解可以重写spring的配置类
 *
 * @author 晓龙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface OverrideProperty {
}
