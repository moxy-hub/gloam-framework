package com.gloamframework.test.proerty.mock.source;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 添加@MappingConfigurationProperties注解，并指定前缀为映射的前缀
 */
@MappingConfigurationProperties("gloam.mapping")
@ConfigurationProperties("gloam.source")
@Data
public class SourceProperties {

    /**
     * 添加@MappingConfigurationProperty表示当前属性进行映射
     */
    @MappingConfigurationProperty
    private String testMapping = "success";

}
