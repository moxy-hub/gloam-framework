package com.gloamframework.core.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 支持在class和method上标示的注解切面拦截器
 *
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月29日 15:48
 */
public abstract class ClassMethodAnnotationPointcutInterceptor<T extends Annotation> extends ClassMethodAnnotationPointcutAdvisor implements MethodInterceptor {

    private final Class<T> annotationClass;

    public ClassMethodAnnotationPointcutInterceptor(Class<T> annotationClass) {
        this(annotationClass, true);
    }

    public ClassMethodAnnotationPointcutInterceptor(Class<T> annotationClass, boolean checkInherited) {
        super(null, annotationClass, checkInherited);
        super.setAdvice(this);
        this.annotationClass = annotationClass;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 获取注解
        T annotation = this.findAnnotation(methodInvocation);
        // 没有获取到则不进行拦截
        if (Objects.isNull(annotation)) {
            return methodInvocation.proceed();
        }
        return this.invokeWithAnnotation(methodInvocation, annotation);
    }

    public abstract Object invokeWithAnnotation(MethodInvocation methodInvocation, T annotation) throws Throwable;

    private T findAnnotation(MethodInvocation methodInvocation) {
        // 获取执行的方法
        Method method = methodInvocation.getMethod();
        // 先尝试在方法是获取
        T dataPermission = AnnotationUtils.findAnnotation(method, annotationClass);
        if (Objects.nonNull(dataPermission)) {
            return dataPermission;
        }
        // 尝试在类上获取,先获取执行的类
        Object targetObject = methodInvocation.getThis();
        Class<?> clazz = Objects.nonNull(targetObject) ? targetObject.getClass() : method.getDeclaringClass();
        return AnnotationUtils.findAnnotation(clazz, annotationClass);
    }
}
