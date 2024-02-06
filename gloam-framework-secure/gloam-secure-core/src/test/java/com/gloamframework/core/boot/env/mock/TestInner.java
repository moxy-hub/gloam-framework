package com.gloamframework.core.boot.env.mock;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

@Data
public class TestInner {

    @MappingConfigurationProperty("url")
    private String url = "66887yyyy";

}
