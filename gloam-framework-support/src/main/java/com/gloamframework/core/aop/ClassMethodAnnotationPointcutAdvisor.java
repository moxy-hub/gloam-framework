package com.gloamframework.core.aop;

import com.gloamframework.core.lang.exception.GloamIllegalArgumentException;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * 支持在class和method上标示的注解切面
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月29日 15:40
 */
public abstract class ClassMethodAnnotationPointcutAdvisor extends AbstractPointcutAdvisor {

    private Advice advice;

    private final Pointcut pointcut;

    public ClassMethodAnnotationPointcutAdvisor(Advice advice, Class<? extends Annotation> annotationClass) {
        this(advice, annotationClass, true);
    }

    public ClassMethodAnnotationPointcutAdvisor(Advice advice, Class<? extends Annotation> annotationClass, boolean checkInherited) {
        if (Objects.isNull(annotationClass)) {
            throw new GloamIllegalArgumentException("[GloamAop]:创建注解切面失败，参数为空");
        }
        this.advice = advice;
        this.pointcut = this.buildPointcut(annotationClass, checkInherited);
    }

    private Pointcut buildPointcut(Class<? extends Annotation> annotationClass, boolean checkInherited) {
        Pointcut classPointcut = new AnnotationMatchingPointcut(annotationClass, checkInherited);
        Pointcut methodPointcut = new AnnotationMatchingPointcut(null, annotationClass, checkInherited);
        return new ComposablePointcut(classPointcut).union(methodPointcut);
    }

    protected void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
