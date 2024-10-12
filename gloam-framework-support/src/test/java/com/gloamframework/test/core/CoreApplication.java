package com.gloamframework.test.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 晓龙
 */
@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Autowired
    public void testMock(Mock2Properties mockProperties) {
        System.out.println(mockProperties);
    }
}
