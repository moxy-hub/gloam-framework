package com.gloamframework.data.properties.pool;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

/**
 * druid配置
 *
 * @author 晓龙
 */
@Data
public class DruidProperties {

    /**
     * 初始连接数
     */
    @MappingConfigurationProperty("initial-size")
    private int initialSize = 5;

    /**
     * 最小连接池数量
     */
    @MappingConfigurationProperty("min-idle")
    private int minIdle = 10;

    /**
     * 最大连接池数量
     */
    @MappingConfigurationProperty("max-active")
    private int maxActive = 20;

    /**
     * 配置获取连接等待超时的时间
     */
    @MappingConfigurationProperty("max-wait")
    private int maxWait = 60000;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    @MappingConfigurationProperty("time-between-eviction-runs-millis")
    private int timeBetweenEvictionRunsMillis = 60000;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    @MappingConfigurationProperty("min-evictable-idle-time-millis")
    private int minEvictableIdleTimeMillis = 300000;

    /**
     * 配置一个连接在池中最大生存的时间，单位是毫秒
     */
    @MappingConfigurationProperty("max-evictable-idle-time-millis")
    private int maxEvictableIdleTimeMillis = 900000;

    /**
     * 配置检测连接是否有效
     */
    @MappingConfigurationProperty("validation-query")
    private String validationQuery = "SELECT 1 FROM DUAL";

    /**
     * 如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效
     */
    @MappingConfigurationProperty("test-while-idle")
    private boolean testWhileIdle = true;

    /**
     * 申请连接时执行validationQuery检测连接是否有效，配置后会降低性能
     */
    @MappingConfigurationProperty("test-on-borrow")
    private boolean testOnBorrow = false;

    /**
     * 归还连接时执行validationQuery检测连接是否有效，配置后会降低性能
     */
    @MappingConfigurationProperty("test-on-return")
    private boolean testOnReturn = false;

    /**
     * 监控统计拦截的filters
     */
    @MappingConfigurationProperty("filters")
    private String filters = "stat,wall,slf4j";

}
