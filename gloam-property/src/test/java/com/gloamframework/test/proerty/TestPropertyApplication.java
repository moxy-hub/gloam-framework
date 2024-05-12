package com.gloamframework.test.proerty;

import com.gloamframework.test.proerty.env.SpringEnvPost;
import com.gloamframework.test.proerty.mock.Mock2Properties;
import com.gloamframework.test.proerty.mock.MockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableConfigurationProperties({MockProperties.class, Mock2Properties.class})
public class TestPropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestPropertyApplication.class, args);
    }

    @Autowired
    public void testMock(MockProperties mockProperties){
        System.out.println(mockProperties);
    }
}
