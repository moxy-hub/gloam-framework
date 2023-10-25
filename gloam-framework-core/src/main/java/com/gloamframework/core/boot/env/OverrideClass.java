package com.gloamframework.core.boot.env;

import java.lang.annotation.*;

/**
 * 重写配置标志类，框架启动时只会对标注了该注解的类进行重写
 *
 * @author 晓龙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface OverrideClass {
}
