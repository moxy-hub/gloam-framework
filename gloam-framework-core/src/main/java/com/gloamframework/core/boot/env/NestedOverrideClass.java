package com.gloamframework.core.boot.env;

import java.lang.annotation.*;

/**
 * 处理嵌套的配置对象
 *
 * @author 晓龙
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface NestedOverrideClass {
}
