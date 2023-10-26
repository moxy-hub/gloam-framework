package com.gloamframework.core.web.doc;

import java.lang.annotation.*;

@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DocGroup {

    String tags();

    String leader() default "unknown";

    String basePackage() default "";

    String title() default "Unknown";

    String version() default "Unknown";

    String description() default "Unknown";

    String serviceUrl() default "Unknown";

}
