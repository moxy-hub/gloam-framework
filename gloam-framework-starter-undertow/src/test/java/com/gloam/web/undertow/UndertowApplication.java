package com.gloam.web.undertow;

import com.gloamframework.web.doc.EnableHttpDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年11月02日 11:01
 */
@SpringBootApplication
@EnableHttpDoc
public class UndertowApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(UndertowApplication.class);
    }

    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void run(String... args) throws Exception {
        ServerProperties.Undertow undertow = serverProperties.getUndertow();
        System.out.println(undertow.toString());
    }
}
