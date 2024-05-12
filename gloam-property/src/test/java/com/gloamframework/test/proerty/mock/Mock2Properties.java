package com.gloamframework.test.proerty.mock;

import com.gloamframework.property.annotation.GloamConfigurationProperties;
import com.gloamframework.property.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
@GloamConfigurationProperties
@ConfigurationProperties("gloam2")
@Data
public class Mock2Properties {


    private DataSize dataSize;

    private Map<String,String> map;
    private Map<String,Mock3Properties> map2;
    private List<String> list;

}
