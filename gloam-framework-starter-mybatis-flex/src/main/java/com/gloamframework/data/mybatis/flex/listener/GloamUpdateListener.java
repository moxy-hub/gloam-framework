package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.domain.BaseDaoDomain;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.annotation.UpdateListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 更新监听
 *
 * @author 晓龙
 */
public class GloamUpdateListener implements UpdateListener {
    @Override
    public void onUpdate(Object metaObject) {
        if (metaObject == null) {
            return;
        }
        if (!BaseDaoDomain.class.isAssignableFrom(metaObject.getClass())) {
            return;
        }
        BaseDaoDomain baseDaoDomain = (BaseDaoDomain) metaObject;
        LocalDateTime currentTime = LocalDateTime.now();
        // 更新时间为空，则以当前时间为更新时间
        if (Objects.isNull(baseDaoDomain.getUpdateTime())) {
            baseDaoDomain.setUpdateTime(currentTime);
        }
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        if (Objects.nonNull(authenticatedUser) && Objects.isNull(baseDaoDomain.getUpdater())) {
            baseDaoDomain.setUpdater(String.valueOf(authenticatedUser));
        }
    }
}
