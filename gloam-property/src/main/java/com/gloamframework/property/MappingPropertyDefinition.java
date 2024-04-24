package com.gloamframework.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 映射的配置定义
 *
 * @author 晓龙
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MappingPropertyDefinition {

    /**
     * 原始路径
     */
    private String originalPath;
    /**
     * 映射路径
     */
    private String mappingPath;
    /**
     * 环境值
     */
    private String value;

}
