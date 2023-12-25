package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.domain.BaseDaoDomain;
import com.gloamframework.web.context.WebContext;
import com.mybatisflex.annotation.InsertListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 晓龙
 */
public class GloamInsertListener implements InsertListener {
    @Override
    public void onInsert(Object metaObject) {
        if (metaObject == null) {
            return;
        }
        if (!BaseDaoDomain.class.isAssignableFrom(metaObject.getClass())) {
            return;
        }
        BaseDaoDomain baseDaoDomain = (BaseDaoDomain) metaObject;
        LocalDateTime currentTime = LocalDateTime.now();
        // 创建时间为空，则以当前时间为插入时间
        if (Objects.isNull(baseDaoDomain.getCreateTime())) {
            baseDaoDomain.setCreateTime(currentTime);
        }
        // 更新时间为空，则以当前时间为更新时间
        if (Objects.isNull(baseDaoDomain.getUpdateTime())) {
            baseDaoDomain.setUpdateTime(currentTime);
        }
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
        if (Objects.nonNull(authenticatedUser) && Objects.isNull(baseDaoDomain.getCreator())) {
            baseDaoDomain.setCreator(String.valueOf(authenticatedUser));
        }
        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        if (Objects.nonNull(authenticatedUser) && Objects.isNull(baseDaoDomain.getUpdater())) {
            baseDaoDomain.setUpdater(String.valueOf(authenticatedUser));
        }
    }
}
