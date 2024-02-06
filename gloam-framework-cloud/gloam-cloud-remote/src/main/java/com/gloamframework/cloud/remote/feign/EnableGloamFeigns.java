package com.gloamframework.cloud.remote.feign;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启gloom对feign做的基础功能，如果只使用原有@EnableFeignClients则gloam所有的配置不会生效
 *
 * @author 晓龙
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/*
 * 这里不把feign开启，考虑到在开启feign时有对feign做的配置，所以交与外部使用者进行自动开启
 */
// @EnableFeignClients
@Import(GloamFeignConfigure.class)
public @interface EnableGloamFeigns {
}
