package com.gloam.web.security;


import com.gloamframework.web.doc.EnableHttpDoc;
import com.gloamframework.web.security.plugin.xss.EnableXssProtect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */

@SpringBootApplication
@EnableHttpDoc
@EnableXssProtect
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class);
    }

}
