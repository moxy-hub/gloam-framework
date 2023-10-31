package com.gloamframework.core.boot.properties.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 用于映射配置到spring环境中的配置
 * <p>1、可以实现配置的重写</p>
 * <p>2、可以实现配置的注入</p>
 *
 * @author 晓龙
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MappingConfigurationProperty {

    /**
     * 默认在使用时传入value既可映射到mappingFor字段上
     */
    @AliasFor("mappingFor")
    String value() default "";

    /**
     * 当前注解标识的字段需要映射的配置全路径
     */
    @AliasFor("value")
    String mappingFor() default "";

    /**
     * 用于处理spring无法识别的配置类型
     * <p>1、自定义对象类型</p>
     * <p>2、Map类型</p>
     * <p>3、Collection类型</p>
     * <p>4、Array类型</p>
     */
    Class<?> nestedPropertyType() default NoNestedConfigurationPropertyType.class;

    /**
     * 用于识别，不进行嵌套处理的配置
     */
    @SuppressWarnings("unused")
    final class NoNestedConfigurationPropertyType {
    }
}
