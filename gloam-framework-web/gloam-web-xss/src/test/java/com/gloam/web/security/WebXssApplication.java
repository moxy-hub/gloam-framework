package com.gloam.web.security;


import com.gloamframework.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */

@SpringBootApplication
@EnableHttpDoc
public class WebXssApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebXssApplication.class);
    }

}