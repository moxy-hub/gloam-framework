package com.gloamframework.data.mybatis.flex.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import lombok.Data;

/**
 * seate分布式事务支持
 *
 * @author 晓龙
 */
@Data
public class SeataProperties {

    /**
     * 是否开启分布式事务，默认关闭
     */
    @MappingConfigurationProperty("enable")
    private boolean enable = false;

    /**
     * 分布式事务模式，默认AT
     */
    @MappingConfigurationProperty("seata-mode")
    private MybatisFlexProperties.SeataMode mode = MybatisFlexProperties.SeataMode.AT;
}
