package com.gloam.web.security;

import com.gloamframework.web.security.chain.annotation.EnableSecurityChain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */
@EnableSecurityChain
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class);
    }

}
