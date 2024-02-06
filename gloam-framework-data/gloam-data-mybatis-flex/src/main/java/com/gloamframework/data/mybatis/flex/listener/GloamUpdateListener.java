package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.properties.FillListenerProperties;
import com.gloamframework.data.mybatis.flex.util.ObjectUtils;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

/**
 * 更新监听
 *
 * @author 晓龙
 */
@Slf4j
public class GloamUpdateListener extends AbstractListener implements UpdateListener {
    @Override
    public void onUpdate(Object metaObject) {
        if (Objects.isNull(metaObject)) {
            log.warn("[MybatisFlex自动填充]:识别对象为null,不进行填充");
            return;
        }
        FillListenerProperties fillListenerProperties = super.obtainProperties();
        Date currentTime = new Date();
        ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdateTimeField(), currentTime);
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        if (Objects.nonNull(authenticatedUser)) {
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdaterField(), String.valueOf(authenticatedUser));
        }
    }
}
