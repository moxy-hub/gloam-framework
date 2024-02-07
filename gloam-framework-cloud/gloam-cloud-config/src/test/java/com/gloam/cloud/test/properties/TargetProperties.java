package com.gloam.cloud.test.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年02月07日 10:42
 */
@Data
@ConfigurationProperties("gloam.properties")
public class TargetProperties {

    @MappingConfigurationProperty("spring.properties.value")
    private String newValue;
}
