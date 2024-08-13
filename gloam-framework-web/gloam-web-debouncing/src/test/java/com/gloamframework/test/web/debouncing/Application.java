package com.gloamframework.test.web.debouncing;

import com.gloamframework.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableHttpDoc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
