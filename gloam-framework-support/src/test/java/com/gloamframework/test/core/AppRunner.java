package com.gloamframework.test.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年08月06日 17:22
 */
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private List<TestObject> testObject;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(testObject);
    }
}
