package com.gloamframework.core.boot.env.convert;

import java.lang.annotation.*;

/**
 * gloam转换器注解，标识了将会进行注册，请确保转换器已实现GenericConverter接口
 *
 * @author 晓龙
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GloamConverter {
}
