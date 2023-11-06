package com.gloamframework.core.http;

import com.gloamframework.core.http.annotation.WebServiceInject;
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

    @WebServiceInject
    private HttpTestBean httpTestBean;

    @Override
    public void run(String... args) throws Exception {
        TestModel token = httpTestBean.token("authorization_code", "http://10.8.11.246/#/login", "222");
        System.out.println(token.getCode());
        System.out.println(token.getMessage());
    }
}
