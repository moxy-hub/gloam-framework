package com.gloamframework.scanner.annotation;

import java.lang.annotation.*;

/**
 * 资源标注注解，标注了该注解的类会被扫描，并通知给调用者
 *
 * @author 晓龙
 * @see com.gloamframework.scanner.ResourceCentre
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GloamResource {

    /**
     * 资源分组，使用了相同的分组的资源可以在{@link com.gloamframework.scanner.ResourceCentre}资源中心进行获取
     * <p>默认为：DEFAULT</p>
     */
    String group() default "";

}
