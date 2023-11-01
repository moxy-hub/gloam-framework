package com.gloamframework.core.boot.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;

//@Configuration
//@EnableConfigurationProperties(TestOverrideClass.class)
public class TestEnvConfiguration {

    @Autowired
    public void test(MultipartProperties multipartProperties) {
        System.out.println(multipartProperties);
    }
}
