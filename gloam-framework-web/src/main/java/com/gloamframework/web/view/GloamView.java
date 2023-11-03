package com.gloamframework.web.view;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.gloamframework.common.lang.BeanUtil;

import java.util.Map;


/**
 * gloam视图
 *
 * @author 晓龙
 */
public class GloamView extends FastJsonJsonView {

    public <M> GloamView(M model) {
        // 将model解析为map
        Map<String, ?> modelMap = BeanUtil.beanToMap(model);
        if (CollectionUtil.isNotEmpty(modelMap)) {
            super.setAttributesMap(modelMap);
        }
    }

}
