package com.gloamframework.test.core.aop;

import com.gloamframework.core.aop.ClassMethodAnnotationPointcutInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月29日 16:03
 */
@Component
public class TestInterceptor extends ClassMethodAnnotationPointcutInterceptor<TestAopAnnotation> {
    public TestInterceptor() {
        super(TestAopAnnotation.class);
    }

    @Override
    public Object invokeWithAnnotation(MethodInvocation methodInvocation, TestAopAnnotation annotation) throws Throwable {
        System.out.println("======= 前面 =======");
        Object proceed = methodInvocation.proceed();
        System.out.println("======= 后面 =======");
        System.out.println("======= 注解:" + annotation + " =======");
        return proceed;
    }

}
