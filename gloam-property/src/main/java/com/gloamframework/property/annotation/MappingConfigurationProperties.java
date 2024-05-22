package com.gloamframework.property.annotation;

import com.gloamframework.scanner.annotation.GloamResource;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static com.gloamframework.property.annotation.MappingConfigurationProperties.GloamConfigurationResource;

/**
 * Gloam资源扫描的配置文件，如果使用@{@link MappingConfigurationProperty}注解，需要配合当前注解使用
 *
 * @author 晓龙
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GloamResource(group = GloamConfigurationResource)
public @interface MappingConfigurationProperties {

    /**
     * 资源分组
     */
    String GloamConfigurationResource = "GloamConfigurationResource";

    @AliasFor("mappingPrefix")
    String value() default "";

    @AliasFor("value")
    String mappingPrefix() default "";

}
