package com.gloamframework.core.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注册bean，并执行目标bean的替换，注意目标bean的类型必须一致
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年08月06日 15:18
 */
@Bean
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReplaceBean {

    /**
     * 替换的bean目标名字，多个目标可以以数组传入
     */
    String[] targets();

    /**
     * 强力替换，true会将替换的目标bean删除，false则是会调整Primary参数
     */
    boolean force() default true;

    @AliasFor(annotation = Bean.class, attribute = "name")
    String[] value() default {};

    @AliasFor(annotation = Bean.class, attribute = "value")
    String[] name() default {};

    @AliasFor(annotation = Bean.class)
    boolean autowireCandidate() default true;

    @AliasFor(annotation = Bean.class)
    String initMethod() default "";

    @AliasFor(annotation = Bean.class)
    String destroyMethod() default "(inferred)";

}
