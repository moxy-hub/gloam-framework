package com.gloamframework.core.boot.properties;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年12月05日 14:18
 * spring.main.allow-bean-definition-overriding=true
 */
@Data
@ConfigurationProperties("gloam.main")
@MappingConfigurationProperties("spring.main")
public class GloamSpringProperties {

    @MappingConfigurationProperty
    private boolean allowBeanDefinitionOverriding = false;

}
