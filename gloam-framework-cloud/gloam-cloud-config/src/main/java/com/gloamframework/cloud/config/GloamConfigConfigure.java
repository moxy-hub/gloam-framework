package com.gloamframework.cloud.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;
import com.gloamframework.cloud.config.locator.GloamPropertySourceLocator;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * gloam 配置
 *
 * @author 晓龙
 */
@Configurable
public class GloamConfigConfigure {

    @Bean
    @Primary
    public NacosPropertySourceLocator gloamPropertySourceLocator(NacosConfigManager nacosConfigManager) {
        return new GloamPropertySourceLocator(nacosConfigManager);
    }
}
