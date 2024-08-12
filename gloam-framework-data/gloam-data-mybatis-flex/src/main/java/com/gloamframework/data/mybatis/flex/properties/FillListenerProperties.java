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

    /**
     * 是否为WEB环境，web环境下创建者和更新者才会被填充，并确保项目中有gloam-web依赖
     */
    private boolean webEnv = true;

}
