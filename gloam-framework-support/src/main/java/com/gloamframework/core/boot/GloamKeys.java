package com.gloamframework.core.boot;

import com.gloamframework.property.PropertyMapperDefinitionSet;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月27日 10:32
 */
public class GloamKeys {

    /**
     * mapping配置映射的定义集合存储key
     */
    public static final GloamObjectKey<PropertyMapperDefinitionSet> MAPPING_DEFINITIONS_KEY = GloamObjectKey.of("MAPPING_DEFINITIONS_KEY", PropertyMapperDefinitionSet.class);

}
