package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.env.mock.TestOverrideClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.Compression;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestOverrideClass.class)
public class TestEnvConfiguration {

    @Autowired
    public void test(ServerProperties multipartProperties){
        System.out.println(multipartProperties);
    }
}
