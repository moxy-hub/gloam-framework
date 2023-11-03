package com.framework.web;

import com.gloamframework.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableHttpDoc
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }
}
