package com.gloamframework.core.http.annotation;

import okhttp3.Interceptor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebService {

    @AliasFor("remoteURL")
    String value() default "";

    @AliasFor("value")
    String remoteURL() default "";

    Class<Interceptor>[] interceptors() default {};
}
