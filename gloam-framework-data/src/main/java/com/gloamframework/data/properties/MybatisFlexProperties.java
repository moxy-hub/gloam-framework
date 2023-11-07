package com.gloamframework.data.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * MybatisFlex配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("com.gloam.data")
@Data
public class MybatisFlexProperties {

    private static final String DEFAULT_DATASOURCE_DYNAMIC_PRIMARY = "master";

    /**
     * mapper接口扫描的基础包路径,默认com.gloam.**.mapper
     */
    private String mapperScanPackage = "com.gloam.**.mapper";

    /**
     * 配置主数据源
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("mybatis-flex.datasource." + DEFAULT_DATASOURCE_DYNAMIC_PRIMARY)
    private DataSourceProperties masterDatasource;

    /**
     * 多数据源,通常用于读写分离的场景 但是并不推荐在应用里使用多数据源,会增加系统的复杂度和配置维护难度 应该交由Mycat数据库中间件统一管理跨库的问题
     */
    @MappingConfigurationProperty(value = "mybatis-flex.datasource.", nestedPropertyType = DataSourceProperties.class)
    private Map<String, DataSourceProperties> datasource;

}
