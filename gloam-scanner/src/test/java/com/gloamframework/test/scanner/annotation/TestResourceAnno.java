package com.gloamframework.test.scanner.annotation;

import com.gloamframework.scanner.annotation.GloamResource;

import java.lang.annotation.*;

/**
 * 添加@GloamResource注解，获取资源扫描能力
 */
@GloamResource
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestResourceAnno {
}
