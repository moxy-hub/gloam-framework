package com.gloamframework.test.core.aop;

import org.springframework.stereotype.Service;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月29日 16:06
 */

@Service
//@TestAopAnnotation("11122")
public class TestAopBean {

    @TestAopAnnotation("aaaaa")
    public void test() {
        System.out.println("方法执行");
    }
}
