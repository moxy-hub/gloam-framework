package com.gloamframework.core.boot.env.mock;

import com.gloamframework.core.boot.env.OverrideClass;
import com.gloamframework.core.boot.env.OverrideProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OverrideClass
@ConfigurationProperties("com.moxy")
@Data
public class TestOverrideClass {

    @OverrideProperty("server.port")
    private Integer t2;

    @OverrideProperty(value = "cc.ee")
    private Map<String,String> v1;

    @OverrideProperty(value = "com.oo",nestedPropertyType = TestInner.class)
    private Map<String,TestInner> v2;

    @OverrideProperty(value = "com.list",nestedPropertyType = TestInner.class)
    private List<TestInner> v3;

    @OverrideProperty("server.tomcat.additional-tld-skip-patterns")
    private List<String> v4;

    public TestOverrideClass(){
        v1 = new HashMap<>();
        v1.put("1","sddsd");
        v1.put("3","sds");
        v1.put("2","vfvf");
        v4 = new ArrayList<>();
        v4.add("444");
        v4.add("888");
    }
}
