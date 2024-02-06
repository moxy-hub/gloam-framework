package com.gloamframework.data.properties.pool;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;

/**
 * hikari配置
 *
 * @author 晓龙
 */
@Data
public class HikariProperties {

    /**
     * 链接超时,单位毫秒,默认30秒
     */
    @MappingConfigurationProperty("connection-timeout")
    private int connectionTimeout = 30000;

    /**
     * 是否只读,默认false
     */
    @MappingConfigurationProperty("is-read-only")
    private boolean readOnly = false;

    /**
     * 最大链接数,默认10 对于比较吃数据资源的项目,应该从设计上改善性能,例如重构并发原理等,建议不要超过30
     */
    @MappingConfigurationProperty("max-pool-size")
    private int maxPoolSize = 10;

    /**
     * 链接保活数量,默认5
     */
    @MappingConfigurationProperty("min-idle")
    private int minIdle = 5;

    /**
     * 最大闲置时间,单位毫秒,默认闲置45秒之后释放数据库连接
     */
    @MappingConfigurationProperty("idle-timeout")
    private int idleTimeout = 45000;

}
