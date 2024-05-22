package com.gloamframework.test.proerty.mock;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月22日 15:25
 */
@MappingConfigurationProperties("gloam.test")
@ConfigurationProperties
@Data
public class DocTestProperties {

    private Integer limit;

}
