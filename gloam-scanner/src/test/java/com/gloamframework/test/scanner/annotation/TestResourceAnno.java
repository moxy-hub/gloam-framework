package com.gloamframework.test.scanner.annotation;

import com.gloamframework.scanner.annotation.GloamResource;

import java.lang.annotation.*;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年04月22日 16:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GloamResource
public @interface TestResourceAnno {
}
