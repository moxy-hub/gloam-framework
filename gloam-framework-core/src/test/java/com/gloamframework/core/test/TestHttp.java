package com.gloamframework.core.test;

import org.springframework.stereotype.Component;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月02日 21:26
 */
@Component
public interface TestHttp {

    default void out() {
        System.out.println("out");
    }

}
