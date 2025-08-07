package com.gloamframework.test.core;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 16:25
 */
@MappingConfigurationProperties
@ConfigurationProperties("gloam")
@Data
@Component
@Service
public class MockProperties {

    @MappingConfigurationProperty("gloam2.data-size")
    private DataSize dataSize = DataSize.ofMegabytes(50);

}
