package com.gloamframework.data.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * gloam数据源配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.data")
@Data
public class GloamDataProperties {

    /**
     * 默认的主数据源名字
     */
    private static final String DEFAULT_DATASOURCE_DYNAMIC_PRIMARY = "master";

    /**
     * 指定默认的数据源，只会对配置进行重写，不会由spring进行配置
     */
    @MappingConfigurationProperty("spring.datasource.dynamic.primary")
    private final String primaryDatasourceName = DEFAULT_DATASOURCE_DYNAMIC_PRIMARY;

    /**
     * 配置主数据源相关配置
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("spring.datasource.dynamic.datasource." + DEFAULT_DATASOURCE_DYNAMIC_PRIMARY)
    private DataSourceProperties masterDatasource;

    /**
     * 多数据源配置
     */
    @MappingConfigurationProperty(value = "spring.datasource.dynamic.datasource", nestedPropertyType = DataSourceProperties.class)
    private Map<String, DataSourceProperties> datasource;

}
