package com.gloam.web.security;

import com.gloamframework.web.security.chain.annotation.EnableSecurityChain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 晓龙
 */
@EnableSecurityChain
@SpringBootApplication
@ComponentScan("com.gloamframework.web.security.chain.assembler")
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class);
    }

}
