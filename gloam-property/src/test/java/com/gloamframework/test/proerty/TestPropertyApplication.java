package com.gloamframework.test.proerty;

import com.gloamframework.test.proerty.mock.DocTestProperties;
import com.gloamframework.test.proerty.mock.Mock2Properties;
import com.gloamframework.test.proerty.mock.MockProperties;
import com.gloamframework.test.proerty.mock.source.MappingProperties;
import com.gloamframework.test.proerty.mock.source.SourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableConfigurationProperties({MockProperties.class, Mock2Properties.class, DocTestProperties.class, MappingProperties.class, SourceProperties.class})
public class TestPropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestPropertyApplication.class, args);
    }

    @Autowired
    public void testMock(MockProperties mockProperties) {
        System.out.println(mockProperties);
    }

    @Autowired
    public void testAnnotaion(DocTestProperties mockProperties) {
        System.out.println(mockProperties);
    }
}
