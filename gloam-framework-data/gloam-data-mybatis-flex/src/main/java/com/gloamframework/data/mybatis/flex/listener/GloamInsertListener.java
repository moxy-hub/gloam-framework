package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.properties.FillListenerProperties;
import com.gloamframework.data.mybatis.flex.util.ObjectUtils;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.annotation.InsertListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

/**
 * @author 晓龙
 */
@Slf4j
public class GloamInsertListener extends AbstractListener implements InsertListener {

    @Override
    public void onInsert(Object metaObject) {
        if (Objects.isNull(metaObject)) {
            log.warn("[MybatisFlex自动填充]:识别对象为null,不进行填充");
            return;
        }
        Date currentTime = new Date();
        FillListenerProperties fillListenerProperties = super.obtainProperties();
        ObjectUtils.fillField(metaObject, fillListenerProperties.getCreateTimeField(), currentTime);
        ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdateTimeField(), currentTime);
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        if (Objects.nonNull(authenticatedUser)) {
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            ObjectUtils.fillField(metaObject, fillListenerProperties.getCreatorField(), String.valueOf(authenticatedUser));
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdaterField(), String.valueOf(authenticatedUser));
        }
    }

}
