package com.gloamframework.data.tenant.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 多租户保护接口
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface TenantProtect {
}
