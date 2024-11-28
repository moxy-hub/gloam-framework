package com.gloamframework.core.boot.context;

import com.gloamframework.property.PropertyMapperDefinitionSet;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2024年11月27日 10:32
 */
public class GloamKeys {

    /**
     * gloam的环境配置变量名
     */
    public static final GloamObjectKey<String> GLOAM_ENV_NAME_SPACE = GloamObjectKey.of("GLOAM_ENV_NAME_SPACE", String.class);

    /**
     * mapping配置映射的定义集合存储key
     */
    public static final GloamObjectKey<PropertyMapperDefinitionSet> MAPPING_DEFINITIONS_KEY = GloamObjectKey.of("MAPPING_DEFINITIONS_KEY", PropertyMapperDefinitionSet.class);


}
