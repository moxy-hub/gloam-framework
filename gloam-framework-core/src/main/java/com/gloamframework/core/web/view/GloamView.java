package com.gloamframework.core.web.view;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;

import java.util.Map;


/**
 * gloam视图
 *
 * @author 晓龙
 */
public class GloamView extends FastJsonJsonView {

    public <M> GloamView(M model) {
        // 通过json序列化方式进行，将model解析为map
        String modelJson = JSON.toJSONString(model);
        @SuppressWarnings("unchecked")
        Map<String, ?> modelMap = JSON.parseObject(modelJson, Map.class);
        if (CollectionUtil.isNotEmpty(modelMap)) {
            super.setAttributesMap(modelMap);
        }
    }

}
