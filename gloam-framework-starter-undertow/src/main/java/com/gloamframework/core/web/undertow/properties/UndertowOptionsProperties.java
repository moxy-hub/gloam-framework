package com.gloamframework.core.web.undertow.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * undertow没有被spring抽象的配置设置
 *
 * @author 晓龙
 */
@Data
public class UndertowOptionsProperties {

    /**
     * spring boot 没有抽象的xnio相关配置在这里配置，对应 org.xnio.Options 类
     */
    @MappingConfigurationProperty("socket")
    private Map<String, String> socket = new LinkedHashMap<>();

    /**
     * spring boot 没有抽象的undertow相关配置在这里配置，对应 io.undertow.UndertowOptions 类
     */
    @MappingConfigurationProperty("server")
    private Map<String, String> server = new LinkedHashMap<>();

}
