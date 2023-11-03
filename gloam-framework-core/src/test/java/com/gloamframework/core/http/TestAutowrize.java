package com.gloamframework.core.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月03日 11:50
 */
@Component
public class TestAutowrize implements CommandLineRunner {

    @Autowired
    private HttpTestBean httpTestBean;


    @Override
    public void run(String... args) throws Exception {
        httpTestBean.list("hello");
    }
}
