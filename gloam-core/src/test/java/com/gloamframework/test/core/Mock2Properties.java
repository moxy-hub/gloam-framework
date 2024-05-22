package com.gloamframework.test.core;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 16:25
 */
@MappingConfigurationProperties
@ConfigurationProperties("gloam2")
@Data
@Component
public class Mock2Properties {


    private DataSize dataSize;

}
