package com.gloamframework.service.tenant.annotation;

import com.gloamframework.service.tenant.TenantConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 开启多租户
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Import(TenantConfigure.class)
public @interface EnableTenant {
}
