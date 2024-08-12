package com.gloamframework.data.mybatis.flex.listener;

import com.gloamframework.data.mybatis.flex.properties.FillListenerProperties;
import com.gloamframework.data.mybatis.flex.util.ObjectUtils;
import com.mybatisflex.annotation.InsertListener;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
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
        LocalDateTime currentTime = LocalDateTime.now();
        FillListenerProperties fillListenerProperties = super.obtainProperties();
        ObjectUtils.fillField(metaObject, fillListenerProperties.getCreateTimeField(), currentTime);
        ObjectUtils.fillField(metaObject, fillListenerProperties.getUpdateTimeField(), currentTime);
        if (fillListenerProperties.isWebEnv()) {
            GloamWebUserListener.fillWebUser(metaObject, true, fillListenerProperties);
        }
    }

}
