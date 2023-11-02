package com.gloam.web.undertow;

import com.gloamframework.core.web.doc.EnableHttpDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月02日 11:01
 */
@SpringBootApplication
@EnableHttpDoc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
    
}
