package com.gloamframework.web.security.chain.annotation;

import java.lang.annotation.*;

/**
 * 通过标识该注解注册装配器
 * @author 晓龙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface SecurityChainAssemblerRegister {
}
