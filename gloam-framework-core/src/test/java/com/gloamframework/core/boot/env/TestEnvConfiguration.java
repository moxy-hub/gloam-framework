package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.env.mock.TestOverrideClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestOverrideClass.class)
public class TestEnvConfiguration {

}
