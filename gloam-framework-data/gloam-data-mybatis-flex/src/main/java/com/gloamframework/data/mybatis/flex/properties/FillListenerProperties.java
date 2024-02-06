package com.gloamframework.data.mybatis.flex.properties;

import lombok.Data;

/**
 * 自动填充相关配置
 *
 * @author 晓龙
 */
@Data
public class FillListenerProperties {

    private String createTimeField = "createTime";

    private String updateTimeField = "updateTime";

    private String creatorField = "creator";

    private String updaterField = "updater";

}
