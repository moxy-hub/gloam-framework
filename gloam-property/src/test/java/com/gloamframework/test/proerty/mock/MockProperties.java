package com.gloamframework.test.proerty.mock;

import com.gloamframework.property.annotation.MappingConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年05月12日 16:25
 */
@MappingConfigurationProperties("gloam")
@ConfigurationProperties("gloam")
@Data
public class MockProperties {

    @MappingConfigurationProperty("spring.test1.t1")
    private String test = "222";

    @MappingConfigurationProperty("gloam2.data-size")
    private DataSize dataSize = DataSize.ofMegabytes(50);

    @MappingConfigurationProperty("gloam2.list")
    private List<String> list;

    @MappingConfigurationProperty("gloam2.map")
    private Map<String,String> map;

    @MappingConfigurationProperty("gloam2.map2")
    private Map<String,Mock3Properties> map2;

    public MockProperties(){
        list = new ArrayList<>();
        list.add("ss");
        list.add("dfgd3");
        list.add("sdfsd");
        map = new HashMap<>();
        map.put("a","123");
        map.put("b","3657");
        map.put("c","343");
    }
    public static class Mock2{

        @MappingConfigurationProperty("t2")
        private Integer t3 = 2333;
    }
}
