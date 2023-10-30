package com.gloamframework.core.boot.env;

import org.springframework.core.annotation.AliasFor;

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
    @AliasFor("target")
    String value() default "";

    @AliasFor("value")
    String target() default "";

    Class<?> nestedPropertyType() default NoNestedProperty.class;

    /**
     * 用于识别，不进行嵌套处理的配置
     */
    @SuppressWarnings("unused")
    final class NoNestedProperty {
    }

}
