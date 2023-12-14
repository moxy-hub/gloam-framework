package com.gloamframework.web.security.plugin.envelope;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 启动XSS保护
 *
 * @author 晓龙
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Import(WebEnvelopeConfigure.class)
public @interface EnableWebEnvelopeProtect {
}
