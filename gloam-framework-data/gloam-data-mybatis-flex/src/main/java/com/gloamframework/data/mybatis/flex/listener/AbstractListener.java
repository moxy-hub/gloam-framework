package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.core.boot.context.SpringContext;
import com.gloamframework.data.mybatis.flex.properties.FillListenerProperties;
import com.gloamframework.data.mybatis.flex.properties.MybatisFlexProperties;

import java.util.Objects;

/**
 * @author 晓龙
 */
public abstract class AbstractListener {

    private FillListenerProperties fillListenerProperties;

    protected FillListenerProperties obtainProperties() {
        if (Objects.nonNull(this.fillListenerProperties)) {
            return this.fillListenerProperties;
        }
        MybatisFlexProperties mybatisFlexProperties = SpringContext.getContext().getBean(MybatisFlexProperties.class);
        fillListenerProperties = mybatisFlexProperties.getFillListener();
        return fillListenerProperties;
    }

}
