package com.gloamframework.core;

import com.gloamframework.core.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHttpDoc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
