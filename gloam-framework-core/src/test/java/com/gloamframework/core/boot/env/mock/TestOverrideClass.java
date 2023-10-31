package com.gloamframework.core.boot.env.mock;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("com.moxy")
@Data
public class TestOverrideClass {

    @MappingConfigurationProperty("server.port")
    private Integer t2;

    @MappingConfigurationProperty(value = "cc.ee")
    private Map<String, String> v1;

    @MappingConfigurationProperty(value = "com.oo", nestedPropertyType = TestInner.class)
    private Map<String, TestInner> v2;

    @MappingConfigurationProperty(value = "com.list", nestedPropertyType = TestInner.class)
    private List<TestInner> v3;

    @MappingConfigurationProperty("server.tomcat.additional-tld-skip-patterns")
    private List<String> v4;

    public TestOverrideClass() {
        v1 = new HashMap<>();
        v1.put("1", "sddsd");
        v1.put("3", "sds");
        v1.put("2", "vfvf");
        v4 = new ArrayList<>();
        v4.add("444");
        v4.add("888");
    }
}
