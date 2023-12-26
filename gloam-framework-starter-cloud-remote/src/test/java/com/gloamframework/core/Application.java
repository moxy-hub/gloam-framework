package com.gloamframework.core;

import com.gloamframework.cloud.remote.feign.EnableGloamFeigns;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableGloamFeigns
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
