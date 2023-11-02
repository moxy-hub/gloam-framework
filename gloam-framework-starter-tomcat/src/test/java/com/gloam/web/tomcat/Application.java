package com.gloam.web.tomcat;

import com.gloamframework.core.web.doc.EnableHttpDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableHttpDoc
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void run(String... args) throws Exception {
        ServerProperties.Tomcat tomcat = serverProperties.getTomcat();
        System.out.println(tomcat.toString());
    }
}
