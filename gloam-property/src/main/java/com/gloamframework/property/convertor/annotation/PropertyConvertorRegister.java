package com.gloamframework.property.convertor.annotation;

import com.gloamframework.scanner.annotation.GloamResource;

import java.lang.annotation.*;

import static com.gloamframework.property.convertor.annotation.PropertyConvertorRegister.GloamPropertyConvertorResource;

/**
 * 标注该注解的类，表示注册为转换器，必须实现接口:{@link com.gloamframework.property.convertor.PropertyConvertor}
 *
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
