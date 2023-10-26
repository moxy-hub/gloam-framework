package com.gloamframework.core.web.doc;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HttpDocConfigure.class)
public @interface EnableHttpDoc {
}
