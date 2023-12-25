package com.gloamframework.web.view;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.gloamframework.common.lang.BeanUtil;

import java.util.Map;


/**
 * gloam视图
 *
 * @author 晓龙
 */
public class GloamView extends FastJsonJsonView {

    private final Map<String, ?> modelMap;

    public <M> GloamView(M model) {
        // 将model解析为map
        modelMap = BeanUtil.beanToMap(model);
        if (CollectionUtil.isNotEmpty(modelMap)) {
            super.setAttributesMap(modelMap);
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(modelMap);
    }

}
