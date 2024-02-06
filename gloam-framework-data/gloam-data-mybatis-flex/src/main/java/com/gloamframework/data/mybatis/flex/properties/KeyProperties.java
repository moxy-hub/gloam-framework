package com.gloamframework.data.mybatis.flex.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

/**
 * mybatis flex主键配置
 *
 * @author 晓龙
 */
@Data
public class KeyProperties {

    /**
     * ID生成策略,默认自增
     */
    @MappingConfigurationProperty("key-type")
    private KeyType keyType = KeyType.Auto;

    /**
     * 使用id生成器的name，或者sequence执行的SQL内容
     */
    @MappingConfigurationProperty("value")
    private String value;

    /**
     * 是否在数据插入前执行，仅在非auto模式下有效
     */
    @MappingConfigurationProperty("before")
    private boolean before = true;

}
