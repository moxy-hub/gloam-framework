package com.gloamframework.test.proerty.mock.source;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月22日 15:46
 */
@ConfigurationProperties("gloam.source")
@MappingConfigurationProperties("gloam.mapping")
@Data
public class SourceProperties {

    @MappingConfigurationProperty
    private String testMapping = "success";

}
