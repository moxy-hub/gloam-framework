package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.env.mock.TestOverrideClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestOverrideClass.class)
public class TestEnvConfiguration {

}
