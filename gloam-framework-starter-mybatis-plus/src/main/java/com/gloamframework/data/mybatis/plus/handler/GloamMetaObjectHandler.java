package com.gloamframework.data.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gloamframework.data.mybatis.plus.domain.BaseDaoDomain;
import com.gloamframework.web.context.WebContext;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 自动填充
 *
 * @author 晓龙
 */
public class GloamMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时的自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject == null) {
            return;
        }
        if (!BaseDaoDomain.class.isAssignableFrom(metaObject.getOriginalObject().getClass())) {
            return;
        }
        BaseDaoDomain baseDaoDomain = (BaseDaoDomain) metaObject.getOriginalObject();
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

    /**
     * 更新时填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updater", metaObject);
        Object authenticatedUser = WebContext.getAuthenticatedUser();
        if (Objects.nonNull(authenticatedUser) && Objects.isNull(modifier)) {
            setFieldValByName("updater", authenticatedUser, metaObject);
        }
    }
}
