package com.gloamframework.property.convertor.annotation;

import com.gloamframework.scanner.annotation.GloamResource;

import java.lang.annotation.*;

import static com.gloamframework.property.convertor.annotation.PropertyConvertorRegister.GloamPropertyConvertorResource;

/**
 * @author 晓龙
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GloamResource(group = GloamPropertyConvertorResource)
public @interface PropertyConvertorRegister {

    /**
     * 资源分组
     */
    String GloamPropertyConvertorResource = "GloamPropertyConvertorResource";
}
