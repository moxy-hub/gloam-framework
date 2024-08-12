package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.properties.FillListenerProperties;
import com.gloamframework.data.mybatis.flex.util.ObjectUtils;
import com.gloamframework.web.context.WebContext;

import java.util.Objects;

/**
 * @author 晓龙
 * 将web独立，防止缺少依赖导致的ClassNotFound异常
 */
public class GloamWebUserListener {

    public static void fillWebUser(Object metaObject, boolean creator, FillListenerProperties fillListenerProperties) {
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        if (Objects.nonNull(authenticatedUser)) {
            if (creator) {
                // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                ObjectUtils.fillField(metaObject, fillListenerProperties.getCreatorField(), String.valueOf(authenticatedUser));
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdaterField(), String.valueOf(authenticatedUser));
        }
    }
}
