package com.gloamframework.core.boot.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * spring上下文感知工具，项目启动会自行进行感知，使用时只需要静态点击即可
 * Spring工具已废弃，迁移方案：{@link GloamContext}
 *
 * @author 晓龙
 */
@Slf4j
@Deprecated
public class SpringContext {

    public static ApplicationContext getContext() {
        return GloamContext.obtainSpringContext();
    }

}
