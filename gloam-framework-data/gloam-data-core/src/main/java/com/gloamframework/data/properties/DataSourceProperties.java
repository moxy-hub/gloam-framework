package com.gloamframework.data.properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import com.gloamframework.data.properties.pool.DruidProperties;
import com.gloamframework.data.properties.pool.HikariProperties;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author 晓龙
 */
@Data
public class DataSourceProperties {

    /**
     * 数据源访问URL,必填
     */
    @MappingConfigurationProperty("url")
    private String url;

    /**
     * 数据源用户,必填
     */
    @MappingConfigurationProperty("username")
    private String username;

    /**
     * 数据源密码,必填
     */
    @MappingConfigurationProperty("password")
    private String password;

    /**
     * 驱动，默认为mysql
     */
    @MappingConfigurationProperty("driver-class-name")
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 连接池方式，默认为druid
     */
    @MappingConfigurationProperty("type")
    private Class<? extends DataSource> type = DruidDataSource.class;

    /**
     * druid配置
     */
    @MappingConfigurationProperty("druid")
    @NestedConfigurationProperty
    private final DruidProperties druid = new DruidProperties();

    /**
     * hikari配置，系统默认引入druid，如果使用hikari，请导入相关依赖
     */
    @MappingConfigurationProperty("hikari")
    @NestedConfigurationProperty
    private final HikariProperties hikari = new HikariProperties();

}
