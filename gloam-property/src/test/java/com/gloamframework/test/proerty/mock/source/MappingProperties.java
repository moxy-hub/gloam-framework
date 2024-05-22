package com.gloamframework.test.proerty.mock.source;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("gloam.mapping")
@Data
public class MappingProperties {

    private String testMapping;

}
