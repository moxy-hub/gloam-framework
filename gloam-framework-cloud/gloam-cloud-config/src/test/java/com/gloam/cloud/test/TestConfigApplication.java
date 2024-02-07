package com.gloam.cloud.test;

import com.gloam.cloud.test.properties.OrginProperties;
import com.gloam.cloud.test.properties.TargetProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 晓龙
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({OrginProperties.class, TargetProperties.class})
public class TestConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestConfigApplication.class, args);
    }

}
