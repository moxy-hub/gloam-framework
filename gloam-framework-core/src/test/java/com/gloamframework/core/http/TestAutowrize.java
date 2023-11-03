package com.gloamframework.core.http;

import com.gloamframework.core.http.annotation.WebServiceInject;
import com.gloamframework.core.http.manager.OkHttpClientManager;
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

    @WebServiceInject
    private HttpTestBean httpTestBean2;

    @WebServiceInject
    private OkHttpClientManager okHttpClientManager;

    @Override
    public void run(String... args) throws Exception {
        String list = httpTestBean.list();
        System.out.println(list);
    }
}
